package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.ProductConverter;
import com.agri_supplies_shop.dto.request.ProductRequest;
import com.agri_supplies_shop.dto.request.SearchProductRequest;
import com.agri_supplies_shop.dto.response.*;
import com.agri_supplies_shop.entity.*;
import com.agri_supplies_shop.enums.Status;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.CategoryRepository;
import com.agri_supplies_shop.repository.ImageRepository;
import com.agri_supplies_shop.repository.ProductRepository;
import com.agri_supplies_shop.repository.SupplierRepository;
import com.agri_supplies_shop.service.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;

    CategoryRepository categoryRepository;

    SupplierRepository supplierRepository;

    ProductConverter productConverter;

    VariantValueService variantService;

    ProductVariantValueService productVariantValueService;

    AttributeService attributeService;

    ImageService imageService;

    ImageRepository imageRepository;

    @Override
    @Transactional
    public ProductResponse createAndUpdate(ProductRequest productRequest) {
        Product product;
        if (productRequest.getId() != null) {
            //Update
            product = productRepository.findById(productRequest.getId()).orElseThrow(
                    () -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)
            );
            product = productConverter.toExistsEntity(productRequest, product);
        } else {
            //create
            product = productConverter.toEntity(productRequest);
        }

        if (Objects.equals(productRequest.getStatus(), "ACTIVE")) {
            product.setStatus(Status.ACTIVE);
        } else {
            product.setStatus(Status.INACTIVE);
        }

        //Get category entity
        Category category = categoryRepository.findById(productRequest.getCategoryId()).orElseThrow(
                () -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)
        );
        product.setCategory(category);

        //Save product
        productRepository.save(product);

        //Save attribute
        Long productId = product.getId();
        List<AttributeResponse> attributeResponses =
                productRequest.getAttributes().stream().map(
                        it ->
                                attributeService.create(it, productId)
                ).toList();
        //Save variant
        productRequest.getVariants().forEach(
                it ->
                        variantService.create(it)
        );

        //save product variant value
        List<ProductVariantValueResponse> productVariantValueResponses =
                productRequest.getVariantValues().stream().map(
                        it ->
                                productVariantValueService.create(it, productId)
                ).toList();
        //To response
        ProductResponse response = productConverter.toResponse(product);
        response.setAttributes(attributeResponses);
        response.setVariants(productVariantValueResponses);
        return response;
    }

    @Override
    @Transactional
    public void delete(List<Long> ids) {
        ids.forEach(
                it -> {
                    Product product = productRepository.findById(it).orElseThrow(
                            () -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)
                    );
                    List<Long> pVariantIds = product.getProductVariantValues().stream().map(
                            pVariantValue -> pVariantValue.getId()
                    ).toList();
                    Boolean stateDel = productVariantValueService.delete(pVariantIds);
                    if (stateDel) {
                        productRepository.deleteById(it);
                    }
                }
        );
    }

    @Override
    public PageResponse<ProductResponse> find(SearchProductRequest searchProductRequest, int page, int size) {
        List<Object> results = productRepository.findProduct(searchProductRequest, page, size);
        Long totalElement = (Long) results.get(1);
        List<Product> products = (List<Product>) results.get(0);
        List<ProductResponse> productResponses;
        if (searchProductRequest.isSearchAllStock()) {
            productResponses = products.stream().map(
                    it -> productConverter.toResponse(it)
            ).toList();
        } else {
            productResponses = products.stream().map(
                    it -> productConverter.toResponse(it)
            ).filter(
                    pd -> getStock(pd.getId()) != 0L
            ).toList();
        }
        return PageResponse.<ProductResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalElements(totalElement)
                .totalPage((int) (Math.ceil((double) totalElement / size)))
                .data(
                        productResponses
                )
                .build();
    }

    @Override
    public ProductResponse findById(Long id) {
        return productConverter.toResponse(productRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)
        ));
    }

    @Override
    public List<ImageResponse> uploadImages(List<MultipartFile> files, Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)
        );
        List<ImageResponse> imageResponses = files.stream().map(
                it -> {
                    try {
                        ImageResponse pathUrl = imageService.saveImage(it);
                        ProductImage productImage = new ProductImage();
                        productImage.setPath(pathUrl.getFilePath());
                        productImage.setProduct(product);
                        imageRepository.save(productImage);
                        return ImageResponse.builder().filePath(pathUrl.getFilePath()).build();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        ).toList();
        return imageResponses;
    }

    @Override
    public ImageResponse uploadThumbnail(MultipartFile file, Long id) throws IOException {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)
        );
        ImageResponse pathUrl = imageService.saveImage(file);
        product.setThumbnail(pathUrl.getFilePath());
        productRepository.save(product);
        return pathUrl;
    }

    @Override
    public Long getStock(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)
        );
        //reserved
        Long reserved = 0L;
        if (product.getProductVariantValues() != null) {
            for (ProductVariantValue pv : product.getProductVariantValues()) {
                if (pv.getReserved() != null)
                    reserved += pv.getReserved();
            }
//            reserved = product.getProductVariantValues().stream().reduce(
//                    0L, (res, pv) -> res + pv.getReserved(),
//                    Long::sum
//            );
        }
        if (product.getProductVariantValues() != null) {
            List<Shipment> shipmentNoExpiry = new ArrayList<>();
            List<Shipment> shipmentHasExpiry = new ArrayList<>();
            for (ProductVariantValue pv : product.getProductVariantValues()) {
                if (!pv.getShipments().isEmpty() && pv.getShipments().get(0).getExpiry() != null) {
                    shipmentHasExpiry.addAll(pv.getShipments().stream().filter(
                            sm ->
                                    Optional.ofNullable(sm).map(Shipment::getExpiry).filter(
                                            expiry -> expiry.isAfter(LocalDate.now())
                                    ).isPresent()
                    ).toList());
                } else {
                    shipmentNoExpiry.addAll(pv.getShipments());
                }
            }
            List<Shipment> shipments = Stream.of(shipmentNoExpiry, shipmentHasExpiry)
                    .flatMap(Collection::stream).toList();
            List<WarehouseDetail> warehouseDetails = shipments.stream().flatMap(
                    it -> it.getWarehouseDetails().stream()
            ).toList();
            Long stock = warehouseDetails.stream().reduce(
                    0L, (partialAgeResult, wd) -> partialAgeResult + wd.getStock(), Long::sum
            );
            return stock - reserved;
        }
        return 0L;
    }


}

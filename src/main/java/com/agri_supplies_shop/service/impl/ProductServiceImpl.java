package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.ProductConverter;
import com.agri_supplies_shop.dto.request.ProductRequest;
import com.agri_supplies_shop.dto.request.SearchProductRequest;
import com.agri_supplies_shop.dto.response.*;
import com.agri_supplies_shop.entity.Category;
import com.agri_supplies_shop.entity.Product;
import com.agri_supplies_shop.entity.ProductImage;
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
import java.util.List;

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
        return PageResponse.<ProductResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalElements(totalElement)
                .totalPage((int) (Math.ceil((double) totalElement / size)))
                .data(
                        products.stream().map(
                                it -> productConverter.toResponse(it)
                        ).toList()
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


}

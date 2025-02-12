package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.ProductConverter;
import com.agri_supplies_shop.dto.request.ProductRequest;
import com.agri_supplies_shop.dto.request.SearchProductRequest;
import com.agri_supplies_shop.dto.response.AttributeResponse;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.dto.response.ProductResponse;
import com.agri_supplies_shop.dto.response.ProductVariantValueResponse;
import com.agri_supplies_shop.entity.Category;
import com.agri_supplies_shop.entity.Product;
import com.agri_supplies_shop.entity.Supplier;
import com.agri_supplies_shop.enums.Origin;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.CategoryRepository;
import com.agri_supplies_shop.repository.ProductRepository;
import com.agri_supplies_shop.repository.SupplierRepository;
import com.agri_supplies_shop.service.AttributeService;
import com.agri_supplies_shop.service.ProductService;
import com.agri_supplies_shop.service.ProductVariantValueService;
import com.agri_supplies_shop.service.VariantValueService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public ProductResponse createAndUpdate(ProductRequest productRequest) {
        Product product;
        if (productRequest.getId() != null) {
            //Update
            product = productRepository.findById(productRequest.getId()).orElseThrow(
                    () -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)
            );
            productConverter.toExistsEntity(productRequest, product);
        } else {
            //create
            product = productConverter.toEntity(productRequest);
        }
        product.setOrigin(Origin.valueOf(productRequest.getOrigin()));

        //Get category entity
        Category category = categoryRepository.findById(productRequest.getCategoryId()).orElseThrow(
                () -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)
        );
        product.setCategory(category);
        //Get supplier entity
        Supplier supplier = supplierRepository.findById(productRequest.getSupplierId()).orElseThrow(
                () -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND)
        );
        product.setSupplier(supplier);
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
        List<Product> products = productRepository.findProduct(searchProductRequest, page, size);
        return PageResponse.<ProductResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalElements(products.size())
                .totalPage((products.size() / size) + 1)
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


}

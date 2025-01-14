package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.ProductConverter;
import com.agri_supplies_shop.dto.request.ProductRequest;
import com.agri_supplies_shop.dto.response.AttributeResponse;
import com.agri_supplies_shop.dto.response.ProductResponse;
import com.agri_supplies_shop.dto.response.ProductVariantValueResponse;
import com.agri_supplies_shop.entity.*;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ProductConverter productConverter;

    @Autowired
    private VariantValueService variantService;

    @Autowired
    private ProductVariantValueService productVariantValueService;

    @Autowired
    private AttributeService attributeService;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = productConverter.toProductEntity(productRequest);
        product.setOrigin(Origin.valueOf(productRequest.getOrigin()));

        //Get category entity
        Category category = categoryRepository.findById(productRequest.getCategoryId()).orElseThrow(
                () -> new AppException(ErrorCode.NOT_FOUND_CATEGORY)
        );
        product.setCategory(category);
        //Get supplier entity
        Supplier supplier = supplierRepository.findById(productRequest.getSupplierId()).orElseThrow(
                () -> new AppException(ErrorCode.NOT_FOUND_SUPPLIER)
        );
        product.setSupplier(supplier);
        //Save product
        productRepository.save(product);

        //Save attribute
        List<AttributeResponse> attributeResponses =
                productRequest.getAttributes().stream().map(
                        it ->
                                attributeService.createAttribute(it, product)
                ).toList();
        //Save variant
        productRequest.getVariants().forEach(
                it ->
                        variantService.createVariantValue(it)
        );

        //save product variant value
        List<ProductVariantValueResponse> productVariantValueResponses =
                productRequest.getVariantValues().stream().map(
                it ->
                    productVariantValueService.createProductVariantValue(it, product)
        ).toList();
        ProductResponse response = productConverter.toProductResponse(product);
        response.setAttributes(attributeResponses);
        response.setVariants(productVariantValueResponses);
        return response;

    }
}

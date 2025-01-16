package com.agri_supplies_shop.service.impl;


import com.agri_supplies_shop.converter.ProductVariantValueConverter;
import com.agri_supplies_shop.dto.request.VariantValueRequest;
import com.agri_supplies_shop.dto.response.ProductVariantValueResponse;
import com.agri_supplies_shop.entity.Product;
import com.agri_supplies_shop.entity.ProductVariantValue;
import com.agri_supplies_shop.entity.VariantValue;
import com.agri_supplies_shop.enums.Status;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.ProductVariantValueRepository;
import com.agri_supplies_shop.repository.VariantValueRepository;
import com.agri_supplies_shop.service.ProductVariantValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductVariantValueImpl implements ProductVariantValueService {
    @Autowired
    private VariantValueRepository variantValueRepository;

    @Autowired
    private ProductVariantValueRepository productVariantValueRepository;

    @Autowired
    private ProductVariantValueConverter variantValueConverter;

    @Override
    @Transactional
    public ProductVariantValueResponse createProductVariantValue(VariantValueRequest variantValueRequest, Product product) {
        //Product variant value
        ProductVariantValue productVariantValue;
        if (variantValueRequest.getId() != null) {
            productVariantValue = productVariantValueRepository.findById(variantValueRequest.getId()).orElseThrow(
                    () -> new AppException(ErrorCode.PRODUCT_VARIANT_VALUE_NOT_FOUND)
            );
            productVariantValue = variantValueConverter.fromRequestToProductVariantValueEntity(variantValueRequest, productVariantValue);

        } else {
            productVariantValue = variantValueConverter.toProductVariantValueEntity(variantValueRequest);
        }
        //Create sku
        List<String> variantIds = variantValueRequest.getVariantCombination().stream().map(
                it -> {
                    VariantValue variantValue = variantValueRepository.findByValue(it);
                    return variantValue.getId().toString();
                }
        ).toList();
        String sku = String.join("-", variantIds);
        productVariantValue.setSku(sku);
        productVariantValue.setProduct(product);
        productVariantValue.setStatus(Status.ACTIVE);
        //Save product variant value
        productVariantValueRepository.save(productVariantValue);


        return variantValueConverter.toProductVariantValueResponse(productVariantValue);
    }
}

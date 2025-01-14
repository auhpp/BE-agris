package com.agri_supplies_shop.service.impl;


import com.agri_supplies_shop.converter.ProductPriceConverter;
import com.agri_supplies_shop.converter.ProductVariantValueConverter;
import com.agri_supplies_shop.dto.request.VariantValueRequest;
import com.agri_supplies_shop.dto.response.ProductVariantValueResponse;
import com.agri_supplies_shop.entity.Product;
import com.agri_supplies_shop.entity.ProductPrice;
import com.agri_supplies_shop.entity.ProductVariantValue;
import com.agri_supplies_shop.entity.VariantValue;
import com.agri_supplies_shop.enums.Status;
import com.agri_supplies_shop.repository.ProductPriceRepository;
import com.agri_supplies_shop.repository.ProductVariantValueRepository;
import com.agri_supplies_shop.repository.VariantValueRepository;
import com.agri_supplies_shop.service.ProductVariantValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductVariantValueImpl implements ProductVariantValueService {
    @Autowired
    private VariantValueRepository variantValueRepository;

    @Autowired
    private ProductPriceConverter productPriceConverter;

    @Autowired
    private ProductVariantValueRepository productVariantValueRepository;

    @Autowired
    private ProductPriceRepository productPriceRepository;

    @Autowired
    private ProductVariantValueConverter variantValueConverter;

    @Override
    @Transactional
    public ProductVariantValueResponse createProductVariantValue(VariantValueRequest variantValueRequest, Product product) {
        //Product variant value
        ProductVariantValue productVariantValue = ProductVariantValue.builder().stock(variantValueRequest.getStock()).build();
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
        //Save product variant value
        productVariantValueRepository.save(productVariantValue);

        //Product price
        ProductPrice productPrice =  productPriceConverter.toProductPriceEntity(variantValueRequest.getProductPriceRequest());
        productPrice.setProductVariantValue(productVariantValue);
        productPrice.setStatus(Status.ACTIVE);
        productPriceRepository.save(productPrice);

        List<ProductPrice> productPrices = new ArrayList<>();
        productPrices.add(productPrice);
        productVariantValue.setProductPrices(productPrices);

        return variantValueConverter.toProductVariantValueResponse(productVariantValue);
    }
}

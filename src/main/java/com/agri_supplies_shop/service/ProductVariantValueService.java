package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.VariantValueRequest;
import com.agri_supplies_shop.dto.response.ProductVariantValueResponse;
import com.agri_supplies_shop.entity.Product;

public interface ProductVariantValueService {
    ProductVariantValueResponse createProductVariantValue(VariantValueRequest variantValueRequest, Product product);
}

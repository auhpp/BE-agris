package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.VariantValueRequest;
import com.agri_supplies_shop.dto.response.ProductVariantValueResponse;
import com.agri_supplies_shop.entity.Product;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public interface ProductVariantValueService {
    ProductVariantValueResponse createProductVariantValue(VariantValueRequest variantValueRequest, Product product);
    void deleteProductVariant(List<Long> ids);
}

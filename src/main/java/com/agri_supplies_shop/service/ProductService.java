package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.ProductRequest;
import com.agri_supplies_shop.dto.response.ProductResponse;

public interface ProductService {
    ProductResponse createAndUpdateProduct(ProductRequest productRequest);
}

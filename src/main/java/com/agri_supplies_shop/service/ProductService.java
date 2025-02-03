package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.ProductRequest;
import com.agri_supplies_shop.dto.request.SearchProductRequest;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.dto.response.ProductResponse;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public interface ProductService {
    ProductResponse createAndUpdateProduct(ProductRequest productRequest);
    void deleteProduct(List<Long> ids);
    PageResponse<ProductResponse> findProduct(SearchProductRequest searchProductRequest, int page, int size);
}

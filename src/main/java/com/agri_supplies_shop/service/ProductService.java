package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.ProductRequest;
import com.agri_supplies_shop.dto.request.SearchProductRequest;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse createAndUpdate(ProductRequest productRequest);
    void delete(List<Long> ids);
    PageResponse<ProductResponse> find(SearchProductRequest searchProductRequest, int page, int size);
    ProductResponse findById(Long id);
}

package com.agri_supplies_shop.repository.custom;

import com.agri_supplies_shop.dto.request.SearchProductRequest;
import com.agri_supplies_shop.dto.response.ProductResponse;
import com.agri_supplies_shop.entity.Product;

import java.util.List;

public interface ProductRepositoryCustom {
    List<Product> findProduct(SearchProductRequest searchProductRequest, int page, int size);
}

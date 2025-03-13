package com.agri_supplies_shop.repository.custom;

import com.agri_supplies_shop.dto.request.SearchProductRequest;
import com.agri_supplies_shop.dto.response.ProductResponse;
import com.agri_supplies_shop.entity.Product;
import jakarta.persistence.Tuple;

import java.util.List;
import java.util.Map;

public interface ProductRepositoryCustom {
    List<Object> findProduct(SearchProductRequest searchProductRequest, int page, int size);
}

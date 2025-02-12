package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.AttributeRequest;
import com.agri_supplies_shop.dto.response.AttributeResponse;
import com.agri_supplies_shop.entity.Product;

import java.util.List;

public interface AttributeService {
    AttributeResponse create(AttributeRequest request, Long productId);
    void deleteById(List<Long> ids);
}

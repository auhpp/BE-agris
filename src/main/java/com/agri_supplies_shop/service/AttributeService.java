package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.AttributeRequest;
import com.agri_supplies_shop.dto.response.AttributeResponse;
import com.agri_supplies_shop.entity.Product;

public interface AttributeService {
    AttributeResponse createAttribute(AttributeRequest attributeRequest, Product product);
}

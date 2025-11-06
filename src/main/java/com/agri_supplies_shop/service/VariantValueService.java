package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.VariantRequest;
import com.agri_supplies_shop.dto.response.VariantResponse;

import java.util.List;

public interface VariantValueService {
    void create(VariantRequest request);

    List<VariantResponse> getAllVariant();

    List<VariantResponse> getVariantValue(String name);
}

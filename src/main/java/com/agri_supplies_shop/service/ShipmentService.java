package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.ShipmentRequest;
import com.agri_supplies_shop.dto.request.ShipmentSearchRequest;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.dto.response.ShipmentResponse;

import java.util.List;

public interface ShipmentService {
    List<ShipmentResponse> findByProductVariantId(Long id);

    ShipmentResponse create(ShipmentRequest request);

    PageResponse<ShipmentResponse> search(ShipmentSearchRequest request, int page, int size);
}

package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.WarehouseRequest;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.dto.response.WarehouseResponse;

public interface WarehouseService {
    WarehouseResponse create(WarehouseRequest request);

    PageResponse<WarehouseResponse> search(String name, int page, int size);

    boolean delete(Long warehouseId);
}

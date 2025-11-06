package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.WarehouseDetailRequest;
import com.agri_supplies_shop.dto.request.WarehouseRequest;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.dto.response.UpdateStockResponse;
import com.agri_supplies_shop.dto.response.WarehouseResponse;

import java.util.List;

public interface WarehouseService {
    WarehouseResponse create(WarehouseRequest request);

    PageResponse<WarehouseResponse> search(String name, int page, int size);

    List<WarehouseResponse> getAll();

    boolean delete(Long warehouseId);

    boolean importWarehouse(Long warehouseReceiptId);

    List<UpdateStockResponse> minusStock(Long quantity, WarehouseDetailRequest request);
}

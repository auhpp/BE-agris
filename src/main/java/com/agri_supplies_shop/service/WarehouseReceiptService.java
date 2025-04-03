package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.WarehouseReceiptRequest;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.dto.response.WarehouseReceiptResponse;

public interface WarehouseReceiptService {
    WarehouseReceiptResponse create(WarehouseReceiptRequest request);

    PageResponse<WarehouseReceiptResponse> getAll(int page, int size);
}

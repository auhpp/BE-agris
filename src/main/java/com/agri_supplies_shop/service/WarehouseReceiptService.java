package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.WarehouseReceiptRequest;
import com.agri_supplies_shop.dto.request.WarehouseReceiptSearchRequest;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.dto.response.WarehouseReceiptResponse;

public interface WarehouseReceiptService {
    WarehouseReceiptResponse create(WarehouseReceiptRequest request);

    PageResponse<WarehouseReceiptResponse> search(WarehouseReceiptSearchRequest request,
                                                  int page, int size);
}

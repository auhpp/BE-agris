package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.ReceiptDetailRequest;
import com.agri_supplies_shop.dto.response.ReceiptDetailResponse;

public interface ReceiptDetailService {
    ReceiptDetailResponse create(ReceiptDetailRequest request, Long warehouseReceiptId);
}

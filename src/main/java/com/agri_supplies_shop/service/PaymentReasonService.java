package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.PaymentReasonRequest;
import com.agri_supplies_shop.dto.response.PaymentReasonResponse;
import com.agri_supplies_shop.entity.PaymentReason;

import java.util.List;

public interface PaymentReasonService {
    PaymentReasonResponse create(PaymentReasonRequest request);
    List<PaymentReasonResponse> getAll();
}

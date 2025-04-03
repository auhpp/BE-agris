package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.PaymentSlipRequest;
import com.agri_supplies_shop.dto.response.PaymentSlipResponse;

public interface PaymentSlipService {
    PaymentSlipResponse create(PaymentSlipRequest request);
}

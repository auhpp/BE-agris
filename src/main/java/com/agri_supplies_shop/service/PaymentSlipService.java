package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.PaymentSlipRequest;
import com.agri_supplies_shop.dto.request.PaymentSlipSearchRequest;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.dto.response.PayeeTypeResponse;
import com.agri_supplies_shop.dto.response.PaymentSlipResponse;

import java.util.List;

public interface PaymentSlipService {
    PaymentSlipResponse create(PaymentSlipRequest request);

    PageResponse<PaymentSlipResponse> search(PaymentSlipSearchRequest request, int page, int size);

    List<PayeeTypeResponse> getAllPayeeType();
}

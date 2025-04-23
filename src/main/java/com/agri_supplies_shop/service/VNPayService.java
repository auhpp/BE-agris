package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.OrderRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface VNPayService {
    //Build redirect payment URL
    String createPaymentUrl(OrderRequest request, HttpServletRequest httpServletRequest) throws UnsupportedEncodingException;

    int orderReturn(HttpServletRequest httpServletRequest);

    void vnpayRefund(OrderRequest request, HttpServletRequest httpServletRequest) throws IOException;
}

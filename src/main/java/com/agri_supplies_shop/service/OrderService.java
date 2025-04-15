package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.CancelOrderRequest;
import com.agri_supplies_shop.dto.request.ConfirmOrderRequest;
import com.agri_supplies_shop.dto.request.OrderRequest;
import com.agri_supplies_shop.dto.request.OrderSearchRequest;
import com.agri_supplies_shop.dto.response.OrderResponse;
import com.agri_supplies_shop.dto.response.PageResponse;

public interface OrderService {
    OrderResponse create(OrderRequest request);

    PageResponse<OrderResponse> search(OrderSearchRequest request, int page, int size);

    OrderResponse confirmOrder(ConfirmOrderRequest request);

    OrderResponse cancelOrder(CancelOrderRequest request);
}

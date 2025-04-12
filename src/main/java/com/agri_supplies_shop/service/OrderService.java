package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.OrderRequest;
import com.agri_supplies_shop.dto.response.OrderResponse;

public interface OrderService {
    OrderResponse create(OrderRequest request);

}

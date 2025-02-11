package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.CartItemRequest;
import com.agri_supplies_shop.dto.response.CartItemResponse;
import com.agri_supplies_shop.dto.response.PageResponse;

import java.util.List;

public interface CartService {
    CartItemResponse addToCart(CartItemRequest request);
    PageResponse<CartItemResponse> getAll(int page, int size);
    void delete(Long cartItemId);
}

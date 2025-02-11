package com.agri_supplies_shop.web.rest;

import com.agri_supplies_shop.dto.request.CartItemRequest;
import com.agri_supplies_shop.dto.response.ApiResponse;
import com.agri_supplies_shop.dto.response.CartItemResponse;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartResource {
    @Autowired
    private CartService cartService;

    @PostMapping
    public ApiResponse<CartItemResponse> addTCart(@RequestBody CartItemRequest request) {
        return ApiResponse.<CartItemResponse>builder()
                .code(200)
                .result(cartService.addToCart(request))
                .build();
    }

    @GetMapping
    public ApiResponse<PageResponse<CartItemResponse>> getAll(@RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                                              @RequestParam(name = "page", required = false, defaultValue = "1") int page
    ) {
        return ApiResponse.<PageResponse<CartItemResponse>>builder()
                .code(200)
                .result(cartService.getAll(page, size))
                .build();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
        cartService.delete(id);
    }
}

package com.agri_supplies_shop.web.rest;

import com.agri_supplies_shop.dto.request.CartItemRequest;
import com.agri_supplies_shop.dto.response.ApiResponse;
import com.agri_supplies_shop.dto.response.CartItemResponse;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.service.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartResource {
    CartService cartService;

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
    public void delete(@PathVariable("id") Long id) {
        cartService.delete(id);
    }

    @GetMapping("/{id}")
    public ApiResponse<CartItemResponse> findById(
            @PathVariable("id") Long id
    ) {
        return ApiResponse.<CartItemResponse>builder()
                .code(200)
                .result(cartService.findById(id))
                .build();
    }
}

package com.agri_supplies_shop.web.rest;

import com.agri_supplies_shop.dto.request.CancelOrderRequest;
import com.agri_supplies_shop.dto.request.ConfirmOrderRequest;
import com.agri_supplies_shop.dto.request.OrderRequest;
import com.agri_supplies_shop.dto.request.OrderSearchRequest;
import com.agri_supplies_shop.dto.response.ApiResponse;
import com.agri_supplies_shop.dto.response.OrderResponse;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderResource {
    OrderService orderService;

    @PostMapping
    public ApiResponse create(@RequestBody OrderRequest request) {
        return ApiResponse.<OrderResponse>builder()
                .code(200)
                .result(orderService.create(request))
                .build();
    }

    @GetMapping("/search")
    public ApiResponse search(OrderSearchRequest request,
                              @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                              @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<OrderResponse>>builder()
                .code(200)
                .result(orderService.search(request, page, size))
                .build();
    }

    @PostMapping("/confirm")
    public ApiResponse confirmOrder(@RequestBody ConfirmOrderRequest request
    ) {
        return ApiResponse.<OrderResponse>builder()
                .code(200)
                .result(orderService.confirmOrder(request))
                .build();
    }

    @PostMapping("/cancel")
    public ApiResponse cancelOrder(@RequestBody CancelOrderRequest request
    ) {
        return ApiResponse.<OrderResponse>builder()
                .code(200)
                .result(orderService.cancelOrder(request))
                .build();
    }

//    @DeleteMapping("/delete/{vnpTxnRef}")
//    public ApiResponse deleteOrder(@PathVariable(name = "vnpTxnRef") String vnpTxnRef){
//        return ApiResponse.builder()
//                .code(200)
//                .build();
//    }


    @GetMapping("/update/payment")
    public ApiResponse updatePaymentStatus(@RequestParam("status") String status,
                                           @RequestParam("vnpTxnRef") String vnpTxnRef
    ) {
        return ApiResponse.<OrderResponse>builder()
                .code(200)
                .result(orderService.updatePaymentStatus(status, vnpTxnRef))
                .build();
    }
}

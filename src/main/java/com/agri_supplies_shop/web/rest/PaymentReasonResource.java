package com.agri_supplies_shop.web.rest;

import com.agri_supplies_shop.dto.request.PaymentReasonRequest;
import com.agri_supplies_shop.dto.response.ApiResponse;
import com.agri_supplies_shop.dto.response.PaymentReasonResponse;
import com.agri_supplies_shop.service.PaymentReasonService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment_reason")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentReasonResource {
    PaymentReasonService paymentReasonService;

    @PostMapping
    public ApiResponse create(@RequestBody PaymentReasonRequest request) {
        return ApiResponse.<PaymentReasonResponse>builder()
                .code(200)
                .result(paymentReasonService.create(request))
                .build();
    }


    @GetMapping
    public ApiResponse getAll(
    ) {
        return ApiResponse.<List<PaymentReasonResponse>>builder()
                .code(200)
                .result(paymentReasonService.getAll())
                .build();
    }
}

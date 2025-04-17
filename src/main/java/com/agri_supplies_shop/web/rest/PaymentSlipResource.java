package com.agri_supplies_shop.web.rest;

import com.agri_supplies_shop.dto.request.PaymentSlipRequest;
import com.agri_supplies_shop.dto.request.PaymentSlipSearchRequest;
import com.agri_supplies_shop.dto.response.ApiResponse;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.dto.response.PayeeTypeResponse;
import com.agri_supplies_shop.dto.response.PaymentSlipResponse;
import com.agri_supplies_shop.service.PaymentSlipService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment_slip")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentSlipResource {
    PaymentSlipService paymentSlipService;

    @PostMapping
    public ApiResponse create(@RequestBody PaymentSlipRequest request) {
        return ApiResponse.<PaymentSlipResponse>builder()
                .code(200)
                .result(paymentSlipService.create(request))
                .build();
    }


    @GetMapping("/search")
    public ApiResponse search(PaymentSlipSearchRequest request,
                              @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                              @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<PaymentSlipResponse>>builder()
                .code(200)
                .result(paymentSlipService.search(request, page, size))
                .build();
    }

    @GetMapping("/payee_type")
    public ApiResponse getAll(
    ) {
        return ApiResponse.<List<PayeeTypeResponse>>builder()
                .code(200)
                .result(paymentSlipService.getAllPayeeType())
                .build();
    }
}

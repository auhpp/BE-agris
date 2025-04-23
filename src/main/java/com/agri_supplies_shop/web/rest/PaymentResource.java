package com.agri_supplies_shop.web.rest;

import com.agri_supplies_shop.dto.request.OrderRequest;
import com.agri_supplies_shop.dto.response.ApiResponse;
import com.agri_supplies_shop.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentResource {
    VNPayService vnPayService;

    @PostMapping("/create_payment_url")
    public ApiResponse<String> createPayment(
            @RequestBody OrderRequest request, HttpServletRequest httpServletRequest
    ) throws UnsupportedEncodingException {
        try {
            return ApiResponse.<String>builder()
                    .code(200)
                    .result(vnPayService.createPaymentUrl(request, httpServletRequest))
                    .build();
        } catch (Exception e) {
            return ApiResponse.<String>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error generating payment URL: " + e.getMessage())
                    .result(vnPayService.createPaymentUrl(request, httpServletRequest))
                    .build();
        }
    }

    @PostMapping("/check_payment")
    public ApiResponse<Integer> checkPayment(
            HttpServletRequest httpServletRequest
    ) {
        return ApiResponse.<Integer>builder()
                .code(200)
                .result(vnPayService.orderReturn(httpServletRequest))
                .build();
    }

    @PostMapping("/refund")
    public ApiResponse vnpayRefund(@RequestBody OrderRequest request,
                                   HttpServletRequest httpServletRequest
    ) throws IOException {
        vnPayService.vnpayRefund(request, httpServletRequest);
        return ApiResponse.builder()
                .code(200)
                .build();
    }
}

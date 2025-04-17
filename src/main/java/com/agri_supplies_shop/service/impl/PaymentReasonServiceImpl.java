package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.dto.request.PaymentReasonRequest;
import com.agri_supplies_shop.dto.response.PaymentReasonResponse;
import com.agri_supplies_shop.entity.PaymentReason;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.PaymentReasonRepository;
import com.agri_supplies_shop.service.PaymentReasonService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentReasonServiceImpl implements PaymentReasonService {
    PaymentReasonRepository paymentReasonRepository;

    @Override
    public PaymentReasonResponse create(PaymentReasonRequest request) {
        PaymentReason paymentReason = paymentReasonRepository.findByName(request.getName());
        if (paymentReason != null) {
            throw new AppException(ErrorCode.PAYMENT_REASON_EXISTED);
        }
        paymentReason = PaymentReason.builder()
                .name(request.getName())
                .build();
        paymentReasonRepository.save(paymentReason);
        return PaymentReasonResponse.builder()
                .name(paymentReason.getName())
                .id(paymentReason.getId())
                .build();
    }

    @Override
    public List<PaymentReasonResponse> getAll() {
        List<PaymentReason> paymentReasons = paymentReasonRepository.findAll();
        if (!paymentReasons.isEmpty()) {
            return paymentReasons.stream().map(
                    it -> PaymentReasonResponse.builder()
                            .name(it.getName())
                            .id(it.getId())
                            .build()
            ).toList();
        }
        return List.of();
    }
}

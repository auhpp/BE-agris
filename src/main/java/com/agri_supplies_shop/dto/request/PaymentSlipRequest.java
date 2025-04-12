package com.agri_supplies_shop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSlipRequest {
    private Long paid;

    private Long outstandingDebt;

    private ZonedDateTime createdDate;

    private String paymentMethod;

    private String note;

    private Long warehouseReceiptId;
}

package com.agri_supplies_shop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSlipResponse {
    private Long id;
    private Long paid;

    private ZonedDateTime createdDate;

    private String paymentMethod;

    private String note;
    private String paymentReason;
    private String payeeType;

    private String payeeName;
}



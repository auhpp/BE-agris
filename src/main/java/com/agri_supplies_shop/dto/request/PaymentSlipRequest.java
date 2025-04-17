package com.agri_supplies_shop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSlipRequest {
    private Long paid;

    private String paymentMethod;

    private String note;

    private Long payeeId;

    private String payeeName;
    private Long paymentReasonId;
    private String reason;
    private boolean debt;
    private Long payeeTypeId;

}

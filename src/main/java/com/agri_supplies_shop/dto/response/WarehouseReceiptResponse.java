package com.agri_supplies_shop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseReceiptResponse {
    private Long id;
    private Long amount;

    private String note;

    private SupplierResponse supplier;

    List<ReceiptDetailResponse> receiptDetails;

    private String paymentMethod;

    private ZonedDateTime createdDate;

    private ZonedDateTime importDate;

    private Long paid;

    private StaffResponse staff;
    private String importStatus;
    private String paymentStatus;

}

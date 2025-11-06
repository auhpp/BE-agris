package com.agri_supplies_shop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseReceiptRequest {

    private Long supplierId;

    private Long warehouseId;

    private Long staffId;

    private Long paid;

    private String note;

    private Long amount;

    List<ReceiptDetailRequest> receiptDetails;

    private String paymentMethod;
}

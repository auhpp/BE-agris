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

    private Long amount;

    private String note;

    private Long moneyForSupplier;

    List<ReceiptDetailRequest> receiptDetails;

    private String paymentMethod;
}

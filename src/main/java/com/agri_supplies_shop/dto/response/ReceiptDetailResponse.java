package com.agri_supplies_shop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptDetailResponse {
    private Long id;
    private ProductVariantValueResponse productVariant;
    private Long unitPrice;
    private List<ShipmentResponse> shipments;

}

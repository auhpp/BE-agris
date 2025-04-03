package com.agri_supplies_shop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptDetailResponse {
    private ProductVariantValueResponse productVariant;
    private Long unitPrice;

    private Long quantity;

}

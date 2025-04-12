package com.agri_supplies_shop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse {
    private Long quantity;
    private Long unitPrice;
    private ProductVariantValueResponse productVariantValue;
}

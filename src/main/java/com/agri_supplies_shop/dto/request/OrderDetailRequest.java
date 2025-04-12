package com.agri_supplies_shop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailRequest {
    private Long quantity;
    private Long unitPrice;
    private Long productVariantId;
}

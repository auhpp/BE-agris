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
public class ProductVariantValueResponse {
    private Long id;
    private int stock;
    private String sku;
    private Long price;
    private Long oldPrice;
    private String discount;
    private List<VariantResponse> variantValues;
}

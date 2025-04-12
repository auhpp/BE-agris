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
    private Long stock;
    private String sku;
    private Long sellingPrice;
    private Long capitalPrice;
    private List<VariantResponse> variantValues;
    private String name;
    private CalculationUnitResponse calculationUnit;
    private String thumbnail;
}

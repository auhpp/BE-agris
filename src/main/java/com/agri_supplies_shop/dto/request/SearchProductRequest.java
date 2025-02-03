package com.agri_supplies_shop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchProductRequest {
    private String name;
    private Long categoryId;
    private Integer priceFrom;
    private Integer priceTo;
}

package com.agri_supplies_shop.dto.request;

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
public class VariantValueRequest {
    private Long id;
    private List<String> variantCombination;
    private Long stock;
    private Long price;

    private ZonedDateTime startDate;

    private ZonedDateTime endDate;

    private Long discount;

    private String discountUnit;
}

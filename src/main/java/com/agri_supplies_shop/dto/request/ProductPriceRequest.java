package com.agri_supplies_shop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductPriceRequest {
    private Long price;

    private ZonedDateTime startDate;

    private ZonedDateTime endDate;

    private Long discount;

    private String discountUnit;

}

package com.agri_supplies_shop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private Long id;

    private String name;

    private String description;

    private String origin;

    private LocalDate productionDate;

    private LocalDate expiry;

    private Long stock;

    private String image;

    private Long categoryId;

    private Long supplierId;

    private List<AttributeRequest> attributes;

    private List<VariantRequest> variants;

    private List<VariantValueRequest> variantValues;

}

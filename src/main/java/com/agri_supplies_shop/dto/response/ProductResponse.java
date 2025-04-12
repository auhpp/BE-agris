package com.agri_supplies_shop.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long id;

    private String name;

    private String description;

    private LocalDate productionDate;

    private LocalDate expiry;

    private String thumbnail;

    private Long stock;

    private CategoryResponse category;

    private List<AttributeResponse> attributes;

    private List<ProductVariantValueResponse> variants;

    private List<ImageResponse> images;

    private String status;

}

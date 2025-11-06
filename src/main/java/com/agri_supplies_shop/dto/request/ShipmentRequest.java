package com.agri_supplies_shop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentRequest {
    private Long id;
    private String name;
    private LocalDate expiry;
    private Long quantity;
    private Long productVariantId;
}

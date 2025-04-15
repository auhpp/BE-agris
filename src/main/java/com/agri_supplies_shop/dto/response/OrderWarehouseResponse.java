package com.agri_supplies_shop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderWarehouseResponse {
    private Long id;
    private String warehouseName;
    private String shipmentName;
    private LocalDate expiry;
}

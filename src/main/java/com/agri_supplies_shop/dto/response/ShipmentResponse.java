package com.agri_supplies_shop.dto.response;

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
public class ShipmentResponse {
    private Long id;
    private String name;
    private LocalDate expiry;
    private Long quantity;
    private String status;
    private List<WarehouseDetailResponse> warehouseDetail;
}

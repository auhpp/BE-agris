package com.agri_supplies_shop.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseDetailRequest {
    private Long productVariantId;
    private List<Long> warehouseDetailId;
}

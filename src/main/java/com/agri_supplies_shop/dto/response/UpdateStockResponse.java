package com.agri_supplies_shop.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStockResponse {
    private Long warehouseDetailId;
    private Long quantity;
}

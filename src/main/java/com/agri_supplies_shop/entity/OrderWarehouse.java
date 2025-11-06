package com.agri_supplies_shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderWarehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Long quantity;

    @ManyToOne
    private OrderDetail orderDetail;

    @ManyToOne
    private WarehouseDetail warehouseDetail;
}

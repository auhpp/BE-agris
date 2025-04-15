package com.agri_supplies_shop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private Long stock;

    //Relationship
    //warehouse
    @ManyToOne
    private Warehouse warehouse;

    //Shipment
    @ManyToOne
    private Shipment shipment;

    //order warehouse
    @OneToMany(mappedBy = "warehouseDetail")
    private List<OrderWarehouse> orderWarehouses;
}

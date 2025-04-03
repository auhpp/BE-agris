package com.agri_supplies_shop.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private int stock;

    //Relationship
    //warehouse
    @ManyToOne
    private Warehouse warehouse;

    //Shipment
    @ManyToOne
    private Shipment shipment;
}

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
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String location;

    private String description;

    //relationship
    //warehouse detail
    @OneToMany(mappedBy = "warehouse")
    private List<WarehouseDetail> warehouseDetails;

    //warehouse receipt
    @OneToMany(mappedBy = "warehouse")
    private List<WarehouseReceipt> warehouseReceipts;
}

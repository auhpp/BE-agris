package com.agri_supplies_shop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    private LocalDate expiry;

    //Relationship
    //Shipment detail
    @OneToMany(mappedBy = "shipment")
    private List<ShipmentDetail> shipmentDetails;

    //warehouse detail
    @OneToMany(mappedBy = "shipment")
    private List<WarehouseDetail> warehouseDetails;

    //Product variant value
    @ManyToOne
    private ProductVariantValue productVariantValue;

}

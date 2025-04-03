package com.agri_supplies_shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private int quantity;

    //Relationship
    //receipt detail
    @ManyToOne
    private ReceiptDetail receiptDetail;

    //shipment
    @ManyToOne
    private Shipment shipment;
}

package com.agri_supplies_shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Long quantity;

    private Long unitPrice;

    //Relationship
    //Order detail
    @ManyToOne
    @JoinColumn(nullable = false)
    private Order order;

    //shipment
    @ManyToOne
    @JoinColumn(nullable = false)
    private Shipment shipment;

}

package com.agri_supplies_shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private Long quantity;

    //Relationship
    //Customer
    @ManyToOne
    @JoinColumn(nullable = false)
    private Customer customer;

    //product variant value
    @ManyToOne
    @JoinColumn(nullable = false)
    private ProductVariantValue productVariantValue;
}

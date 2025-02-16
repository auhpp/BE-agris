package com.agri_supplies_shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private Long quantity;

    //Relationship
    //user
    @ManyToOne
    @JoinColumn(nullable = false)
    private Users user;

    //product variant value
    @ManyToOne
    @JoinColumn(nullable = false)
    private ProductVariantValue productVariantValue;
}

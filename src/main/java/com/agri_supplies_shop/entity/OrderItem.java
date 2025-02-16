package com.agri_supplies_shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Long quantity;

    private Long price;

    //Relationship
    //Order detail
    @ManyToOne
    @JoinColumn(nullable = false)
    private OrderDetail orderDetail;

    //Product variant value
    @ManyToOne
    @JoinColumn(nullable = false)
    private ProductVariantValue productVariantValue;

}

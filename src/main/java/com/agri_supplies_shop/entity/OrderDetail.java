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
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Long quantity;

    private Long unitPrice;

    //Relationship
    //Orders
    @ManyToOne
    @JoinColumn(nullable = false)
    private Orders orders;

    //product variant
    @ManyToOne
    private ProductVariantValue productVariantValue;

    //order warehouse
    @OneToMany(mappedBy = "orderDetail")
    private List<OrderWarehouse> orderWarehouses;

}

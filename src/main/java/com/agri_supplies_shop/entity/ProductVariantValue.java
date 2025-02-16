package com.agri_supplies_shop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantValue {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private Long stock;

    @Column(nullable = false)
    private String sku;

    private String thumbnail;

    @Column(nullable = false)
    private Long price;

    private ZonedDateTime startDate;

    private ZonedDateTime endDate;

    private Long discount;

    private String discountUnit;

    //Relationship
    //cart item
    @OneToMany(mappedBy = "productVariantValue", cascade = {CascadeType.REMOVE})
    private List<CartItem> cartItems;

    //order item
    @OneToMany(mappedBy = "productVariantValue")
    private List<OrderItem> orderItems;

    //product
    @ManyToOne
    @JoinColumn(nullable = false)
    private Product product;
}

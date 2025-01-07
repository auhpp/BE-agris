package com.agri_supplies_shop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Data
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

    @CreatedDate
    private ZonedDateTime createdAt;

    @LastModifiedDate
    private ZonedDateTime updatedAt;

    //Relationship
    //product price
    @OneToMany(mappedBy = "productVariantValue")
    private List<ProductPrice> productPrices;

    //cart item
    @OneToMany(mappedBy = "productVariantValue")
    private List<CartItem> cartItems;

    //order item
    @OneToMany(mappedBy = "productVariantValue")
    private List<OrderItem> orderItems;

    //product
    @ManyToOne
    @JoinColumn(nullable = false)
    private Product product;
}

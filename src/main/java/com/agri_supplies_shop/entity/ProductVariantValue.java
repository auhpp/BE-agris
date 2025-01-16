package com.agri_supplies_shop.entity;

import com.agri_supplies_shop.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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

    @Column(nullable = false)
    private Long price;

    private ZonedDateTime startDate;

    private ZonedDateTime endDate;

    private Long discount;

    private String discountUnit;

    @Enumerated(EnumType.STRING)
    private Status status;

    @CreatedDate
    private ZonedDateTime createdAt;

    @LastModifiedDate
    private ZonedDateTime updatedAt;

    //Relationship
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

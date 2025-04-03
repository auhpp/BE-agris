package com.agri_supplies_shop.entity;

import com.agri_supplies_shop.enums.Status;
import jakarta.persistence.*;
import lombok.*;

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
    private Long sellingPrice;

    @Column(nullable = false)
    private Long capitalPrice;

    private Boolean isShipmentManage;

    @Enumerated(EnumType.STRING)
    private Status status;

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

    //calculation unit
    @ManyToOne
    private CalculationUnit calculationUnit;

    //Shipment
    @OneToMany(mappedBy = "productVariantValue")
    private List<Shipment> shipments;

}

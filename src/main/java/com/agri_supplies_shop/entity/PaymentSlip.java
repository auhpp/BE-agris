package com.agri_supplies_shop.entity;

import com.agri_supplies_shop.enums.PaymentMethod;
import com.agri_supplies_shop.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentSlip {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private Long paid;

    private ZonedDateTime createdDate;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String note;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    //Relationship
    //warehouse receipt
    @ManyToOne
    private WarehouseReceipt warehouseReceipt;
}

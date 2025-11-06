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
public class PaymentReason {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    //Relation
    @OneToMany(mappedBy = "paymentReason")
    private List<PaymentSlip> paymentSlips;
}

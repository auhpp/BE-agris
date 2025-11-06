package com.agri_supplies_shop.entity;

import com.agri_supplies_shop.enums.PayeeTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayeeType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PayeeTypeEnum name;

    //Relationship
    @OneToMany(mappedBy = "payeeType")
    private List<PaymentSlip> paymentSlips;

}

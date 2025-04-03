package com.agri_supplies_shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String phoneNumber;

    private String province;

    private String district;

    private String ward;

    private String deliveryAddress;

    @Column(nullable = false)
    private Boolean defaultChoice;

    //Relationship
    //user
    @ManyToOne
    @JoinColumn(nullable = false)
    private Users user;
}

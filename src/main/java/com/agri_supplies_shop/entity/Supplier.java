package com.agri_supplies_shop.entity;

import com.agri_supplies_shop.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String email;

    private String phoneNumber;

    private String contactName;

    private String address;

    private Long debt;

    @Enumerated(EnumType.STRING)
    private Status status;

    //Relationship
    //warehouse receipt
    @OneToMany(mappedBy = "supplier")
    private List<WarehouseReceipt> warehouseReceipts;
}

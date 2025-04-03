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
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Status status;

    //relationship
    //product
    @OneToMany(mappedBy = "category")
    private List<Product> products;
}


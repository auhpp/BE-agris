package com.agri_supplies_shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String path;

    //Relationship
    //product
    @ManyToOne
    @JoinColumn(nullable = false)
    private Product product;
}

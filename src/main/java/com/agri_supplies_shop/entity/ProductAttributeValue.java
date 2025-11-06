package com.agri_supplies_shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttributeValue {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, columnDefinition = "text")
    private String value;

    //Relationship
    //attribute
    @ManyToOne
    @JoinColumn(nullable = false)
    private Attribute attribute;

    //product
    @ManyToOne
    @JoinColumn(nullable = false)
    private Product product;
}

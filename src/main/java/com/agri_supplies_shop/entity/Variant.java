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
public class Variant {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    //Relationship
    @OneToMany(mappedBy = "variant", cascade = {CascadeType.PERSIST})
    private List<VariantValue> variantValues;
}

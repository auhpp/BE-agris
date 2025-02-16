package com.agri_supplies_shop.entity;

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

    //relationship
    //category
    @ManyToOne
    @JoinColumn
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = {CascadeType.REMOVE})
    private List<Category> categories;

    //product
    @OneToMany(mappedBy = "category")
    private List<Product> products;
}


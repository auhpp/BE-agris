package com.agri_supplies_shop.entity;

import com.agri_supplies_shop.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.ZonedDateTime;
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

    @CreatedDate
    private ZonedDateTime createdAt;

    @LastModifiedDate
    private ZonedDateTime updatedAt;

    //relationship
    //category
    @ManyToOne
    @JoinColumn
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> categories;

    //product
    @OneToMany(mappedBy = "category")
    private List<Product> products;
}


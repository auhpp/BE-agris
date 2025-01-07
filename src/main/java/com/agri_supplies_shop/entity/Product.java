package com.agri_supplies_shop.entity;

import com.agri_supplies_shop.enums.Origin;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Origin origin;

    @Column(nullable = false)
    private ZonedDateTime productionDate;

    @Column(nullable = false)
    private ZonedDateTime expiry;

    @Column(nullable = false)
    private Long stock;

    private Float rating;

    private String image;

    private long totalSold;

    @CreatedDate
    private ZonedDateTime createdAt;

    @LastModifiedDate
    private ZonedDateTime updatedAt;

    //Relationship
    //supplier
    @ManyToOne
    @JoinColumn(nullable = false)
    private Supplier supplier;

    //image
    @OneToMany(mappedBy = "product")
    private List<ProductImage> productImages;

    //product attribute value
    @OneToMany(mappedBy = "product")
    private List<ProductAttributeValue> productAttributeValues;

    //category
    @ManyToOne
    @JoinColumn(nullable = false)
    private Category category;

    //review
    @OneToMany(mappedBy = "product")
    private List<Review> reviews;
}

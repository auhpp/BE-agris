package com.agri_supplies_shop.entity;

import com.agri_supplies_shop.enums.Origin;
import com.agri_supplies_shop.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Getter
@Setter
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
    private LocalDate productionDate;

    @Column(nullable = false)
    private LocalDate expiry;

    @Column(nullable = false)
    private Long stock;

    private Float rating;

    @Column(columnDefinition = "TEXT")
    private String image;

    private long totalSold;

    @CreatedDate
    private ZonedDateTime createdAt;

    @LastModifiedDate
    private ZonedDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    //Relationship
    //supplier
    @ManyToOne
    @JoinColumn(nullable = false)
    private Supplier supplier;

    //image
    @OneToMany(mappedBy = "product", cascade = {CascadeType.REMOVE})
    private List<ProductImage> productImages;

    //product attribute value
    @OneToMany(mappedBy = "product", cascade = {CascadeType.REMOVE})
    private List<ProductAttributeValue> productAttributeValues;

    //category
    @ManyToOne
    @JoinColumn(nullable = false)
    private Category category;

    //review
    @OneToMany(mappedBy = "product", cascade = {CascadeType.REMOVE})
    private List<Review> reviews;

    //product variant value
    @OneToMany(mappedBy = "product")
    private List<ProductVariantValue> productVariantValues;
}

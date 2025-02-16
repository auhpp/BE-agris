package com.agri_supplies_shop.entity;

import com.agri_supplies_shop.enums.Origin;
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

    @Column(nullable = false, columnDefinition = "text")
    private String name;

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Origin origin;

    @Column(nullable = false)
    private LocalDate productionDate;

    @Column(nullable = false)
    private LocalDate expiry;

    @Column(columnDefinition = "TEXT")
    private String thumbnail;

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

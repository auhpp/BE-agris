package com.agri_supplies_shop.entity;

import com.agri_supplies_shop.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, columnDefinition = "text")
    private String name;

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String thumbnail;

    @CreatedDate
    private ZonedDateTime createdAt;

    @LastModifiedDate
    private ZonedDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    //Relationship
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

    //product variant value
    @OneToMany(mappedBy = "product")
    private List<ProductVariantValue> productVariantValues;
}

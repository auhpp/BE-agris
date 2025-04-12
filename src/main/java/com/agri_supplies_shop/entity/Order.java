package com.agri_supplies_shop.entity;

import com.agri_supplies_shop.enums.OrderDetailStatus;
import com.agri_supplies_shop.enums.PaymentMethod;
import com.agri_supplies_shop.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String note;

    @Enumerated(EnumType.STRING)
    private OrderDetailStatus orderStatus;

    private Long amount;

    private String paymentProvider;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @CreatedDate
    private ZonedDateTime createdAt;

    @LastModifiedDate
    private ZonedDateTime updatedAt;

    private PaymentMethod paymentMethod;

    private String fullName;

    private String phoneNumber;

    private String province;

    private String ward;

    private String district;

    private String deliveryAddress;

    //Relationship
    //Customer
    @ManyToOne
    @JoinColumn(nullable = false)
    private Customer customer;

    //Order item
    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;

}

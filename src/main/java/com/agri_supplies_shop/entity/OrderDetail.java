package com.agri_supplies_shop.entity;

import com.agri_supplies_shop.enums.OrderDetailStatus;
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
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String note;

    @Enumerated(EnumType.STRING)
    private OrderDetailStatus status;

    @CreatedDate
    private ZonedDateTime createdAt;

    @LastModifiedDate
    private ZonedDateTime updatedAt;

    //Relationship
    //user
    @ManyToOne
    @JoinColumn(nullable = false)
    private Users user;

    //Payment detail
    @OneToOne
    @JoinColumn(nullable = false)
    private PaymentDetail paymentDetail;

    //Order item
    @OneToMany(mappedBy = "orderDetail")
    private List<OrderItem> orderItems;

}

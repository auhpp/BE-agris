package com.agri_supplies_shop.dto.response;


import com.agri_supplies_shop.enums.OrderStatus;
import com.agri_supplies_shop.enums.PaymentMethod;
import com.agri_supplies_shop.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;

    private String note;

    private Long amount;

    private String paymentProvider;

    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;

    private PaymentMethod paymentMethod;

    private CustomerResponse customer;

    private String fullName;

    private String phoneNumber;

    private String province;

    private String ward;

    private String district;

    private String deliveryAddress;
    List<OrderDetailResponse> orderDetails;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    private String reasonForCancellation;

    private String canceller;

}

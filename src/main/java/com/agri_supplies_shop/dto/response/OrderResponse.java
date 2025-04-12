package com.agri_supplies_shop.dto.response;


import com.agri_supplies_shop.dto.request.AddressRequest;
import com.agri_supplies_shop.enums.OrderDetailStatus;
import com.agri_supplies_shop.enums.PaymentMethod;
import com.agri_supplies_shop.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;

    private String note;

    private Long amount;

    private String paymentProvider;

    private OrderDetailStatus orderStatus;
    private PaymentStatus paymentStatus;

    private PaymentMethod paymentMethod;

    private String fullName;

    private CustomerResponse customer;

    private AddressResponse address;


}

package com.agri_supplies_shop.dto.request;

import com.agri_supplies_shop.entity.Customer;
import com.agri_supplies_shop.entity.OrderDetail;
import com.agri_supplies_shop.enums.PaymentMethod;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private Long id;

    private String note;

    private Long amount;

    private String paymentMethod;

    private String orderStatus;

    private AddressRequest addressRequest;

    private Long customerId;

    List<OrderDetailRequest> orderDetails;
}

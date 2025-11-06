package com.agri_supplies_shop.converter;

import com.agri_supplies_shop.dto.request.OrderRequest;
import com.agri_supplies_shop.dto.response.OrderDetailResponse;
import com.agri_supplies_shop.dto.response.OrderResponse;
import com.agri_supplies_shop.dto.response.OrderWarehouseResponse;
import com.agri_supplies_shop.entity.Orders;
import com.agri_supplies_shop.enums.PaymentMethod;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderConverter {
    ModelMapper modelMapper;
    AddressConverter addressConverter;
    CustomerConverter customerConverter;
    ProductVariantValueConverter variantValueConverter;

    public Orders toEntity(OrderRequest request) {
        Orders orders = modelMapper.map(request, Orders.class);
        if (request.getPaymentMethod().equals("CASH"))
            orders.setPaymentMethod(PaymentMethod.CASH);
        else {
            orders.setPaymentMethod(PaymentMethod.TRANSFER);
        }
        orders.setProvince(request.getAddressRequest().getProvince());
        orders.setDistrict(request.getAddressRequest().getDistrict());
        orders.setWard(request.getAddressRequest().getWard());
        orders.setDeliveryAddress(request.getAddressRequest().getDeliveryAddress());
        orders.setFullName(request.getAddressRequest().getFullName());
        orders.setPhoneNumber(request.getAddressRequest().getPhoneNumber());
        orders.setCreatedAt(ZonedDateTime.now());
        return orders;
    }

    public void toExistedEntity(OrderRequest request, Orders orders) {
        if (request.getPaymentMethod().equals("CASH"))
            orders.setPaymentMethod(PaymentMethod.CASH);
        else {
            orders.setPaymentMethod(PaymentMethod.TRANSFER);
        }
        orders.setProvince(request.getAddressRequest().getProvince());
        orders.setDistrict(request.getAddressRequest().getDistrict());
        orders.setWard(request.getAddressRequest().getWard());
        orders.setDeliveryAddress(request.getAddressRequest().getDeliveryAddress());
        orders.setFullName(request.getAddressRequest().getFullName());
        orders.setPhoneNumber(request.getAddressRequest().getPhoneNumber());
    }

    public OrderResponse toResponse(Orders orders) {
        OrderResponse response = modelMapper.map(orders, OrderResponse.class);
        response.setProvince(orders.getProvince());
        response.setDistrict(orders.getDistrict());
        response.setWard(orders.getWard());
        response.setDeliveryAddress(orders.getDeliveryAddress());
        response.setFullName(orders.getFullName());
        response.setPhoneNumber(orders.getPhoneNumber());
        response.setCustomer(customerConverter.toResponse(orders.getCustomer()));
        response.setVnpTxnRef(orders.getVnpTxnRef());
        List<OrderDetailResponse> orderDetails = orders.getOrderDetails().stream().map(
                od -> OrderDetailResponse.builder()
                        .productVariantValue(
                                variantValueConverter.toResponse(od.getProductVariantValue())
                        )
                        .orderWarehouses(
                                od.getOrderWarehouses() != null ?
                                        od.getOrderWarehouses().stream().map(
                                                ow -> OrderWarehouseResponse.builder()
                                                        .id(ow.getId())
                                                        .shipmentName(ow.getWarehouseDetail().getShipment().getName())
                                                        .warehouseName(ow.getWarehouseDetail().getWarehouse().getName())
                                                        .expiry(ow.getWarehouseDetail().getShipment().getExpiry())
                                                        .build()
                                        ).toList() : null
                        )
                        .quantity(od.getQuantity())
                        .unitPrice(od.getUnitPrice())
                        .build()
        ).toList();

        response.setOrderDetails(orderDetails);
        return response;
    }
}

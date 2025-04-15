package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.OrderConverter;
import com.agri_supplies_shop.dto.request.*;
import com.agri_supplies_shop.dto.response.OrderResponse;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.dto.response.UpdateStockResponse;
import com.agri_supplies_shop.entity.*;
import com.agri_supplies_shop.enums.OrderStatus;
import com.agri_supplies_shop.enums.PaymentStatus;
import com.agri_supplies_shop.enums.PredefinedRole;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.*;
import com.agri_supplies_shop.repository.specification.BaseSpecification;
import com.agri_supplies_shop.repository.specification.SearchCriteria;
import com.agri_supplies_shop.service.OrderService;
import com.agri_supplies_shop.service.WarehouseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;
    OrderConverter orderConverter;
    CustomerRepository customerRepository;
    OrderDetailRepository orderDetailRepository;
    WarehouseService warehouseService;
    WarehouseDetailRepository warehouseDetailRepository;
    ProductVariantValueRepository variantValueRepository;
    OrderWarehouseRepository orderWarehouseRepository;
    AccountRepository accountRepository;

    @Override
    @Transactional
    public OrderResponse create(OrderRequest request) {
        Orders orders = orderConverter.toEntity(request);
        orders.setOrderStatus(OrderStatus.WAIT_FOR_CONFIRMATION);
        orders.setPaymentStatus(PaymentStatus.NO_PAYMENT);
        orders.setCreatedAt(ZonedDateTime.now());
        orders.setCustomer(customerRepository.findById(request.getCustomerId()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        ));
        ProductVariantValue productVariantValue = null;
        for (OrderDetailRequest od : request.getOrderDetails()) {
            productVariantValue = variantValueRepository.findById(od.getProductVariantId()).orElseThrow(
                    () -> new AppException(ErrorCode.PRODUCT_VARIANT_VALUE_NOT_FOUND)
            );
            productVariantValue.setReserved(productVariantValue.getReserved() + od.getQuantity());
            variantValueRepository.save(productVariantValue);
        }
        orderRepository.save(orders);
        List<OrderDetail> orderDetails = request.getOrderDetails().stream().map(
                odr -> OrderDetail.builder()
                        .quantity(odr.getQuantity())
                        .unitPrice(odr.getUnitPrice())
                        .productVariantValue(
                                variantValueRepository.findById(odr.getProductVariantId()).orElseThrow(
                                        () -> new AppException(ErrorCode.PRODUCT_VARIANT_VALUE_NOT_FOUND)
                                )
                        )
                        .orders(orders)
                        .build()
        ).toList();
        orderDetailRepository.saveAll(orderDetails);
        orders.setOrderDetails(orderDetails);
        return orderConverter.toResponse(orders);
    }

    @Override
    public PageResponse<OrderResponse> search(OrderSearchRequest request, int page, int size) {
        Specification<Orders> spec = Specification.where(null);
        if (request.getId() != null) {
            BaseSpecification<Orders> specId = new BaseSpecification(
                    SearchCriteria.builder()
                            .key("id")
                            .value(request.getId())
                            .operation("=")
                            .build()
            );
            spec = spec.and(specId);
        }
        if (!Objects.equals(request.getOrderStatus(), "")) {
            BaseSpecification<Orders> specStatus = new BaseSpecification(
                    SearchCriteria.builder()
                            .key("orderStatus")
                            .value(request.getOrderStatus())
                            .operation("=")
                            .build()
            );
            spec = spec.and(specStatus);
        }
        if (!Objects.equals(request.getPaymentStatus(), "")) {
            BaseSpecification<Orders> specPaymentStatus = new BaseSpecification(
                    SearchCriteria.builder()
                            .key("paymentStatus")
                            .value(request.getPaymentStatus())
                            .operation("=")
                            .build()
            );
            spec = spec.and(specPaymentStatus);
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Orders> orders = orderRepository.findAll(spec, pageable);
        return PageResponse.<OrderResponse>builder()
                .currentPage(page)
                .totalElements(orders.getTotalElements())
                .totalPage(orders.getTotalPages())
                .pageSize(orders.getSize())
                .data(
                        orders.stream().map(
                                orderConverter::toResponse
                        ).toList()
                )
                .build();
    }

    @Override
    @Transactional
    public OrderResponse confirmOrder(ConfirmOrderRequest request) {
        Orders orders = orderRepository.findById(request.getOrderId()).orElseThrow(
                () -> new AppException(ErrorCode.ORDER_NOT_EXISTED)
        );
        request.getWarehouseDetails().forEach(
                it -> {
                    OrderDetail orderDetail = orderDetailRepository.findByOrdersIdAndProductVariantValueId(
                            orders.getId(), it.getProductVariantId()
                    );
                    List<UpdateStockResponse> updateStockResponse =
                            warehouseService.minusStock(orderDetail.getQuantity(), it);
                    updateStockResponse.forEach(
                            us -> {
                                OrderWarehouse orderWarehouse = OrderWarehouse.builder()
                                        .orderDetail(orderDetail)
                                        .warehouseDetail(warehouseDetailRepository.findById(
                                                us.getWarehouseDetailId()
                                        ).orElseThrow(() -> new AppException(ErrorCode.WAREHOUSE_NOT_EXISTED)))
                                        .quantity(us.getQuantity())
                                        .build();
                                orderWarehouseRepository.save(orderWarehouse);
                            }
                    );
                }
        );
        orders.setOrderStatus(OrderStatus.WAITING_FOR_SHIPPING);
        orders.setUpdatedAt(ZonedDateTime.now());
        orderRepository.save(orders);
        return orderConverter.toResponse(orders);
    }

    @Override
    public OrderResponse cancelOrder(CancelOrderRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Account account = accountRepository.findByUserName(userName).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        Orders orders = orderRepository.findById(request.getOrderId()).orElseThrow(
                () -> new AppException(ErrorCode.ORDER_NOT_EXISTED)
        );
        orders.setReasonForCancellation(request.getReasonForCancellation());
        List<OrderDetail> orderDetails = orders.getOrderDetails();
        orderDetails.forEach(
                it -> {
                    ProductVariantValue variantValue = it.getProductVariantValue();
                    variantValue.setReserved(
                            it.getProductVariantValue().getReserved() - it.getQuantity()
                    );
                    variantValueRepository.save(variantValue);
                }
        );
        if (orders.getOrderStatus() != OrderStatus.WAIT_FOR_CONFIRMATION) {
            orderDetails.forEach(
                    it -> {
                        it.getOrderWarehouses().forEach(
                                ow -> {
                                    WarehouseDetail warehouseDetail = ow.getWarehouseDetail();
                                    warehouseDetail.setStock(warehouseDetail.getStock() + ow.getQuantity());
                                    warehouseDetailRepository.save(
                                            warehouseDetail
                                    );
                                }
                        );
                    }
            );
        }
        if (account.getRole().equals(PredefinedRole.ADMIN_ROLE)) {
            orders.setCanceller("ADMIN");
        } else {
            orders.setCanceller("CUSTOMER");
        }
        orders.setOrderStatus(OrderStatus.CANCELED);
        orders.setUpdatedAt(ZonedDateTime.now());
        orderRepository.save(orders);
        return null;
    }
}

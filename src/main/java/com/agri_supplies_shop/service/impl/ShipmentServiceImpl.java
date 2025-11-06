package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.ShipmentConverter;
import com.agri_supplies_shop.dto.request.ShipmentRequest;
import com.agri_supplies_shop.dto.request.ShipmentSearchRequest;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.dto.response.ShipmentResponse;
import com.agri_supplies_shop.entity.ProductVariantValue;
import com.agri_supplies_shop.entity.Shipment;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.ProductVariantValueRepository;
import com.agri_supplies_shop.repository.ShipmentRepository;
import com.agri_supplies_shop.repository.specification.BaseSpecification;
import com.agri_supplies_shop.repository.specification.SearchCriteria;
import com.agri_supplies_shop.service.ShipmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShipmentServiceImpl implements ShipmentService {

    ShipmentRepository shipmentRepository;

    ProductVariantValueRepository productVariantValueRepository;

    ShipmentConverter shipmentConverter;

    @Override
    public List<ShipmentResponse> findByProductVariantId(Long id) {
        ProductVariantValue variantValue = productVariantValueRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_VARIANT_VALUE_NOT_FOUND)
        );
        List<Shipment> shipments = variantValue.getShipments();
        if (!shipments.isEmpty()) {
            List<ShipmentResponse> shipmentsRes = shipments.stream().map(
                    it -> shipmentConverter.toResponse(it)
            ).collect(Collectors.toList());
            if (shipmentsRes.get(0).getExpiry() != null) {
                Comparator<ShipmentResponse> comparatorAsc = Comparator.comparing(ShipmentResponse::getExpiry);
                Collections.sort(shipmentsRes, comparatorAsc);
            }
            return shipmentsRes;
        }
        return List.of();
    }

    @Override
    @Transactional
    public ShipmentResponse create(ShipmentRequest request) {
        ProductVariantValue variantValue = productVariantValueRepository.findById(request.getProductVariantId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_VALUE_NOT_FOUND));
        Shipment shipment = shipmentConverter.toEntity(request);
        if (request.getExpiry() == null) {
            shipment.setExpiry(null);
        }
        shipment.setProductVariantValue(variantValue);
        return shipmentConverter.toResponse(
                shipmentRepository.save(shipment)
        );

    }

    @Override
    public PageResponse<ShipmentResponse> search(ShipmentSearchRequest request, int page, int size) {
        BaseSpecification spec1 = new BaseSpecification(
                SearchCriteria.builder()
                        .key("name")
                        .operation(":")
                        .value(request.getName())
                        .build()
        );
        Specification<Shipment> spec = Specification.where(spec1);
        if (request.getWarehouseId() != null) {
            BaseSpecification spec2 = new BaseSpecification(
                    SearchCriteria.builder()
                            .key("id")
                            .operation("=")
                            .value(request.getWarehouseId())
                            .nameObject("warehouse")
                            .nameTableJoin("warehouseDetails")
                            .build()
            );
            spec = spec.and(spec2);
        }

        if (Objects.equals(request.getStatus(), "ACTIVE")) {
            BaseSpecification spec3 = new BaseSpecification(
                    SearchCriteria.builder()
                            .key("expiry")
                            .operation(">")
                            .value(LocalDate.now())
                            .build()
            );
            spec = spec.and(spec3);
        } else if (Objects.equals(request.getStatus(), "INACTIVE")) {
            BaseSpecification spec3 = new BaseSpecification(
                    SearchCriteria.builder()
                            .key("expiry")
                            .operation("<")
                            .value(LocalDate.now())
                            .build()
            );
            spec = spec.and(spec3);
        }
        if (request.getProductVariantId() != null) {
            BaseSpecification spec4 = new BaseSpecification(
                    SearchCriteria.builder()
                            .key("id")
                            .operation("=")
                            .value(request.getProductVariantId())
                            .nameObject("productVariantValue")
                            .build()
            );
            spec = spec.and(spec4);
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Shipment> shipments = shipmentRepository.findAll(spec, pageable);
        List<ShipmentResponse> shipmentResponses = shipments.stream().map(
                it -> {
                    ShipmentResponse res = shipmentConverter.toResponse(it);
                    Long stock = res.getQuantity();
                    if (request.getWarehouseId() != null) {
                        stock = it.getWarehouseDetails().stream().reduce(
                                0L, (result, wd) -> {
                                    if (Objects.equals(wd.getWarehouse().getId(), request.getWarehouseId())) {
                                        return result + wd.getStock();
                                    }
                                    return 0L;
                                },
                                Long::sum
                        );
                    }
                    res.setQuantity(stock);
                    return res;
                }
        ).toList();
        return PageResponse.<ShipmentResponse>builder()
                .data(shipmentResponses)
                .pageSize(shipments.getSize())
                .totalPage(shipments.getTotalPages())
                .totalElements(shipments.getTotalElements())
                .currentPage(page)
                .build();
    }
}

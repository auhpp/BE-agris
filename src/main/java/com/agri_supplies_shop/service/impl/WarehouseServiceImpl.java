package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.WarehouseConverter;
import com.agri_supplies_shop.dto.request.WarehouseRequest;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.dto.response.WarehouseResponse;
import com.agri_supplies_shop.entity.Warehouse;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.WarehouseRepository;
import com.agri_supplies_shop.service.WarehouseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WarehouseServiceImpl implements WarehouseService {

    WarehouseRepository warehouseRepository;

    WarehouseConverter warehouseConverter;

    @Override
    @Transactional
    public WarehouseResponse create(WarehouseRequest request) {
        Warehouse warehouse;
        if (request.getId() != null) {
            warehouse = warehouseRepository.findById(request.getId()).orElseThrow(
                    () -> new AppException(ErrorCode.WAREHOUSE_NOT_EXISTED)
            );
            warehouseConverter.toExistedEntity(warehouse, request);
        } else {
            warehouse = warehouseConverter.toEntity(request);
        }

        return warehouseConverter.toResponse(warehouseRepository.save(warehouse));
    }

    @Override
    public PageResponse<WarehouseResponse> search(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Warehouse> warehouses = warehouseRepository.findByNameContaining(name, pageable);
        return PageResponse.<WarehouseResponse>builder()
                .currentPage(page)
                .totalElements(warehouses.getTotalElements())
                .totalPage(warehouses.getTotalPages())
                .pageSize(warehouses.getSize())
                .data(
                        warehouses.getContent().stream().map(
                                it -> warehouseConverter.toResponse(it)
                        ).toList()
                )
                .build();
    }

    @Override
    @Transactional
    public boolean delete(Long warehouseId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId).orElseThrow(
                () -> new AppException(ErrorCode.WAREHOUSE_NOT_EXISTED)
        );
        if (warehouse.getWarehouseDetails().isEmpty() && warehouse.getWarehouseReceipts().isEmpty()) {
            warehouseRepository.deleteById(warehouse.getId());
            return true;
        }
        return false;
    }
}

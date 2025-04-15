package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.WarehouseConverter;
import com.agri_supplies_shop.dto.request.WarehouseDetailRequest;
import com.agri_supplies_shop.dto.request.WarehouseRequest;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.dto.response.UpdateStockResponse;
import com.agri_supplies_shop.dto.response.WarehouseResponse;
import com.agri_supplies_shop.entity.*;
import com.agri_supplies_shop.enums.ImportGoodsStatus;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.ProductVariantValueRepository;
import com.agri_supplies_shop.repository.WarehouseDetailRepository;
import com.agri_supplies_shop.repository.WarehouseReceiptRepository;
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

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WarehouseServiceImpl implements WarehouseService {

    WarehouseRepository warehouseRepository;

    WarehouseConverter warehouseConverter;

    WarehouseReceiptRepository warehouseReceiptRepository;

    WarehouseDetailRepository warehouseDetailRepository;

    ProductVariantValueRepository productVariantValueRepository;

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
    public List<WarehouseResponse> getAll() {
        return warehouseRepository.findAll().stream().map(
                it -> warehouseConverter.toResponse(it)
        ).toList();
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

    @Override
    public boolean importWarehouse(Long warehouseReceiptId) {
        WarehouseReceipt warehouseReceipt = warehouseReceiptRepository.findById(warehouseReceiptId)
                .orElseThrow(
                        () -> new AppException(ErrorCode.WAREHOUSE_RECEIPT_NOT_EXISTED)
                );
//        WarehouseDetail warehouseDetail = new WarehouseDetail();
        List<ReceiptDetail> receiptDetails = warehouseReceipt.getReceiptDetails();
        //Flat map: trải các element trong  it.getShipmentDetails() thành một stream duy nhất
        List<ShipmentDetail> shipmentDetails = receiptDetails.stream().flatMap(
                it -> it.getShipmentDetails().stream()
        ).toList();
        shipmentDetails.forEach(
                it -> {
                    Shipment shipment = it.getShipment();
                    if (shipment.getWarehouseDetails() != null) {
                        WarehouseDetail warehouseDetail = shipment.getWarehouseDetails().stream().filter(
                                wD -> wD.getWarehouse().getId() == warehouseReceipt.getWarehouse().getId()
                        ).findFirst().orElse(null);
                        if (warehouseDetail != null) {
                            warehouseDetail.setStock(warehouseDetail.getStock() + it.getQuantity());
                            warehouseDetailRepository.save(warehouseDetail);
                        } else {
                            warehouseDetail = new WarehouseDetail();
                            warehouseDetail.setWarehouse(warehouseReceipt.getWarehouse());
                            warehouseDetail.setShipment(shipment);
                            warehouseDetail.setStock(it.getQuantity());
                            warehouseDetailRepository.save(warehouseDetail);
                        }
                    } else {
                        WarehouseDetail detail = new WarehouseDetail();
                        detail.setWarehouse(warehouseReceipt.getWarehouse());
                        detail.setShipment(shipment);
                        detail.setStock(it.getQuantity());
                        warehouseDetailRepository.save(detail);
                    }
                }
        );
        warehouseReceipt.setImportStatus(ImportGoodsStatus.IMPORTED_GOODS);
        warehouseReceipt.setImportDate(ZonedDateTime.now());
        warehouseReceiptRepository.save(warehouseReceipt);
        return true;
    }

    @Override
    @Transactional
    public List<UpdateStockResponse> minusStock(Long quantity, WarehouseDetailRequest whDetails) {
        ProductVariantValue variantValue = productVariantValueRepository.findById(whDetails.getProductVariantId()).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_VARIANT_VALUE_NOT_FOUND)
        );
        List<WarehouseDetail> warehouseDetails = whDetails.getWarehouseDetailId().stream().map(
                it -> warehouseDetailRepository.findById(it).orElseThrow(
                        () -> new AppException(ErrorCode.WAREHOUSE_NOT_EXISTED)
                )
        ).toList();
        Long quantityProduct = quantity;
        List<UpdateStockResponse> updateStockResponses = new ArrayList<>();
        for (WarehouseDetail wd : warehouseDetails) {
            if (wd.getStock() >= quantityProduct) {
                wd.setStock(wd.getStock() - quantityProduct);
                quantityProduct = 0L;
                warehouseDetailRepository.save(wd);
                UpdateStockResponse updateStockResponse = UpdateStockResponse.builder()
                        .warehouseDetailId(wd.getId())
                        .quantity(quantity)
                        .build();
                updateStockResponses.add(updateStockResponse);
                break;
            } else {
                quantityProduct = quantityProduct - wd.getStock();
                wd.setStock(0L);
                warehouseDetailRepository.save(wd);
                UpdateStockResponse updateStockResponse = UpdateStockResponse.builder()
                        .warehouseDetailId(wd.getId())
                        .quantity(quantity - quantityProduct)
                        .build();
                updateStockResponses.add(updateStockResponse);
            }
        }
        if (quantityProduct != 0L) {
            throw new AppException(ErrorCode.INSUFFICIENT_INVENTORY);
        }
        variantValue.setReserved(variantValue.getReserved() - quantity);
        productVariantValueRepository.save(variantValue);
        return updateStockResponses;
    }

}

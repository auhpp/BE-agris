package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.ReceiptDetailConverter;
import com.agri_supplies_shop.dto.request.ReceiptDetailRequest;
import com.agri_supplies_shop.dto.response.ReceiptDetailResponse;
import com.agri_supplies_shop.dto.response.ShipmentResponse;
import com.agri_supplies_shop.entity.*;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.*;
import com.agri_supplies_shop.service.ReceiptDetailService;
import com.agri_supplies_shop.service.ShipmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReceiptDetailServiceImpl implements ReceiptDetailService {
    ReceiptDetailRepository receiptDetailRepository;
    WarehouseReceiptRepository warehouseReceiptRepository;
    ProductVariantValueRepository variantValueRepository;
    ReceiptDetailConverter receiptDetailConverter;
    ShipmentService shipmentService;
    ShipmentRepository shipmentRepository;
    ShipmentDetailRepository shipmentDetailRepository;

    @Override
    @Transactional
    public ReceiptDetailResponse create(ReceiptDetailRequest request, Long warehouseReceiptId) {
        WarehouseReceipt warehouseReceipt = warehouseReceiptRepository.findById(warehouseReceiptId).orElseThrow(
                () -> new AppException(ErrorCode.WAREHOUSE_RECEIPT_NOT_EXISTED)
        );
        ProductVariantValue variantValue = variantValueRepository.findById(request.getProductVariantId()).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_VARIANT_VALUE_NOT_FOUND)
        );
        variantValue.setCapitalPrice(request.getUnitPrice());
        variantValue.setSellingPrice(request.getUnitPrice());
        variantValueRepository.save(variantValue);
        ReceiptDetail receiptDetail = ReceiptDetail.builder()
                .importPrice(request.getUnitPrice())
                .warehouseReceipt(warehouseReceipt)
                .build();
        receiptDetailRepository.save(receiptDetail);
        //Shipment
        List<ShipmentDetail> shipmentDetails = request.getShipments().stream().map(
                it -> {
                    Shipment shipment;
                    if (it.getId() == null) {
                        ShipmentResponse response = shipmentService.create(it);
                        shipment = shipmentRepository.findById(response.getId()).orElseThrow(
                                () -> new AppException(ErrorCode.SHIPMENT_NOT_EXISTED)
                        );
                    } else {
                        shipment = shipmentRepository.findById(it.getId()).orElseThrow(
                                () -> new AppException(ErrorCode.SHIPMENT_NOT_EXISTED)
                        );
                    }
                    ShipmentDetail shipmentDetail = ShipmentDetail.builder()
                            .shipment(shipment)
                            .quantity(it.getQuantity())
                            .receiptDetail(receiptDetail)
                            .build();
                    shipmentDetailRepository.save(shipmentDetail);
                    return shipmentDetail;
                }
        ).toList();
        receiptDetail.setShipmentDetails(shipmentDetails);
        return receiptDetailConverter.toResponse(receiptDetail);
    }
}

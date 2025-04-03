package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.ReceiptDetailConverter;
import com.agri_supplies_shop.dto.request.ReceiptDetailRequest;
import com.agri_supplies_shop.dto.response.ReceiptDetailResponse;
import com.agri_supplies_shop.entity.ProductVariantValue;
import com.agri_supplies_shop.entity.ReceiptDetail;
import com.agri_supplies_shop.entity.WarehouseReceipt;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.ProductVariantValueRepository;
import com.agri_supplies_shop.repository.ReceiptDetailRepository;
import com.agri_supplies_shop.repository.WarehouseReceiptRepository;
import com.agri_supplies_shop.service.ReceiptDetailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReceiptDetailServiceImpl implements ReceiptDetailService {
    ReceiptDetailRepository receiptDetailRepository;
    WarehouseReceiptRepository warehouseReceiptRepository;
    ProductVariantValueRepository variantValueRepository;
    ReceiptDetailConverter receiptDetailConverter;

    @Override
    @Transactional
    public ReceiptDetailResponse create(ReceiptDetailRequest request, Long warehouseReceiptId) {
        WarehouseReceipt warehouseReceipt = warehouseReceiptRepository.findById(warehouseReceiptId).orElseThrow(
                () -> new AppException(ErrorCode.WAREHOUSE_RECEIPT_NOT_EXISTED)
        );
        ProductVariantValue variantValue = variantValueRepository.findById(request.getProductVariantId()).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_VARIANT_VALUE_NOT_FOUND)
        );
        ReceiptDetail receiptDetail = ReceiptDetail.builder()
                .importPrice(request.getUnitPrice())
                .warehouseReceipt(warehouseReceipt)
                .build();
        return receiptDetailConverter.toResponse(receiptDetailRepository.save(receiptDetail));
    }
}

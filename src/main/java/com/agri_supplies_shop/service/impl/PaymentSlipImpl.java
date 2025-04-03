package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.SupplierDebtConverter;
import com.agri_supplies_shop.dto.request.PaymentSlipRequest;
import com.agri_supplies_shop.dto.response.PaymentSlipResponse;
import com.agri_supplies_shop.entity.PaymentSlip;
import com.agri_supplies_shop.entity.WarehouseReceipt;
import com.agri_supplies_shop.enums.PaymentMethod;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.SupplierDebtRepository;
import com.agri_supplies_shop.repository.WarehouseReceiptRepository;
import com.agri_supplies_shop.service.PaymentSlipService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentSlipImpl implements PaymentSlipService {
    SupplierDebtRepository supplierDebtRepository;
    WarehouseReceiptRepository warehouseReceiptRepository;
    SupplierDebtConverter supplierDebtConverter;

    @Override
    @Transactional
    public PaymentSlipResponse create(PaymentSlipRequest request) {
        WarehouseReceipt warehouseReceipt = warehouseReceiptRepository.findById(
                request.getWarehouseReceiptId()
        ).orElseThrow(() -> new AppException(ErrorCode.WAREHOUSE_RECEIPT_NOT_EXISTED));
        PaymentSlip supplierDebt = PaymentSlip.builder()
                .note(request.getNote())
                .createdDate(ZonedDateTime.now())
                .paid(request.getPaid())
                .warehouseReceipt(warehouseReceipt)
                .build();
        if (request.getPaymentMethod() == "CASH") {
            supplierDebt.setPaymentMethod(PaymentMethod.CASH);
        } else if (request.getPaymentMethod() == "TRANSFER") {
            supplierDebt.setPaymentMethod(PaymentMethod.TRANSFER);
        }
        return supplierDebtConverter.toResponse(
                supplierDebtRepository.save(supplierDebt)
        );
    }
}

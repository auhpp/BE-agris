package com.agri_supplies_shop.converter;

import com.agri_supplies_shop.dto.response.WarehouseReceiptResponse;
import com.agri_supplies_shop.entity.PaymentSlip;
import com.agri_supplies_shop.entity.WarehouseReceipt;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WarehouseReceiptConverter {
    ModelMapper modelMapper;
    ReceiptDetailConverter receiptDetailConverter;
    SupplierConverter supplierConverter;

    public WarehouseReceiptResponse toResponse(WarehouseReceipt warehouseReceipt) {
        WarehouseReceiptResponse response = modelMapper.map(warehouseReceipt, WarehouseReceiptResponse.class);
        response.setSupplier(supplierConverter.toResponse(warehouseReceipt.getSupplier()));
        if (warehouseReceipt.getReceiptDetails() != null)
            response.setReceiptDetails(warehouseReceipt.getReceiptDetails().stream().map(
                    it -> receiptDetailConverter.toResponse(it)
            ).toList());
        if (warehouseReceipt.getPaymentSlips() != null) {
            List<PaymentSlip> supplierDebts = warehouseReceipt.getPaymentSlips();
            Comparator<PaymentSlip> comparatorDesc = (prod1, prod2) -> prod2.getCreatedDate()
                    .compareTo(prod1.getCreatedDate());
            Collections.sort(supplierDebts, comparatorDesc);
//            response.setOutstandingDebt(supplierDebts.get(0).getOutstandingDebt());
        } else {
            response.setOutstandingDebt(0L);
        }
        return response;
    }
}

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
    StaffConverter staffConverter;

    public WarehouseReceiptResponse toResponse(WarehouseReceipt warehouseReceipt) {
        WarehouseReceiptResponse response = WarehouseReceiptResponse.builder()
                .id(warehouseReceipt.getId())
                .amount(warehouseReceipt.getAmount())
                .note(warehouseReceipt.getNote())
                .createdDate(warehouseReceipt.getCreatedDate())
                .build();
        if (warehouseReceipt.getPaymentStatus() != null
        ) {
            response.setPaymentMethod(warehouseReceipt.getPaymentStatus().name());
        }
        response.setSupplier(supplierConverter.toResponse(warehouseReceipt.getSupplier()));
        response.setImportStatus(warehouseReceipt.getImportStatus().getName());
        response.setStaff(staffConverter.toResponse(warehouseReceipt.getStaff()));

        if (warehouseReceipt.getReceiptDetails() != null)
            response.setReceiptDetails(warehouseReceipt.getReceiptDetails().stream().map(
                    it -> receiptDetailConverter.toResponse(it)
            ).toList());

//        if (warehouseReceipt.getPaymentSlips() != null) {
//            List<PaymentSlip> paymentSlip = warehouseReceipt.getPaymentSlips();
//            Comparator<PaymentSlip> comparatorDesc = (prod1, prod2) -> prod2.getCreatedDate()
//                    .compareTo(prod1.getCreatedDate());
//            Collections.sort(paymentSlip, comparatorDesc);
//            int sumPaid = 0;
//            for (int i = 0; i < paymentSlip.size(); i++) {
//                sumPaid += paymentSlip.get(0).getPaid();
//            }
//            response.setOutstandingDebt(warehouseReceipt.getAmount() - sumPaid);
//        } else {
//            response.setOutstandingDebt(0L);
//        }
        if (warehouseReceipt.getImportDate() != null) {
            response.setImportDate(warehouseReceipt.getImportDate());
        }
        return response;
    }
}

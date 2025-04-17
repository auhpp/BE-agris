package com.agri_supplies_shop.converter;

import com.agri_supplies_shop.dto.response.PaymentSlipResponse;
import com.agri_supplies_shop.entity.PaymentSlip;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SupplierDebtConverter {
    ModelMapper modelMapper;
    WarehouseReceiptConverter warehouseReceiptConverter;

    public PaymentSlipResponse toResponse(PaymentSlip supplierDebt) {
        PaymentSlipResponse response = modelMapper.map(supplierDebt, PaymentSlipResponse.class);
        return response;
    }
}

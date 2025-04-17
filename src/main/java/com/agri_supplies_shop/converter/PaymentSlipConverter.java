package com.agri_supplies_shop.converter;

import com.agri_supplies_shop.dto.response.PaymentSlipResponse;
import com.agri_supplies_shop.entity.PaymentSlip;
import com.agri_supplies_shop.enums.PayeeTypeEnum;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.CustomerRepository;
import com.agri_supplies_shop.repository.SupplierRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentSlipConverter {
    ModelMapper modelMapper;
    SupplierRepository supplierRepository;
    CustomerRepository customerRepository;

    public PaymentSlipResponse toResponse(PaymentSlip paymentSlip) {
        PaymentSlipResponse response = modelMapper.map(paymentSlip, PaymentSlipResponse.class);
        response.setCreatedDate(paymentSlip.getCreatedDate());
        response.setPaymentMethod(paymentSlip.getPaymentMethod().name());
        response.setPaymentReason(paymentSlip.getPaymentReason().getName());
        response.setPayeeType(paymentSlip.getPayeeType().getName().getName());
        if (paymentSlip.getPayeeId() != null) {
            if (paymentSlip.getPayeeType().getName() == PayeeTypeEnum.SUPPLIER) {
                response.setPayeeName(
                        supplierRepository.findById(paymentSlip.getPayeeId()).orElseThrow(
                                () -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND)
                        ).getName()
                );
            } else if (paymentSlip.getPayeeType().getName() == PayeeTypeEnum.CUSTOMER) {
                response.setPayeeName(
                        customerRepository.findById(paymentSlip.getPayeeId()).orElseThrow(
                                () -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND)
                        ).getFullName()
                );
            }
        } else {
            response.setPayeeName(paymentSlip.getPayeeName());
        }
        return response;
    }
}

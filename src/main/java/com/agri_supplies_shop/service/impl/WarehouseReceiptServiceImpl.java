package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.WarehouseReceiptConverter;
import com.agri_supplies_shop.dto.request.PaymentSlipRequest;
import com.agri_supplies_shop.dto.request.WarehouseReceiptRequest;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.dto.response.ReceiptDetailResponse;
import com.agri_supplies_shop.dto.response.WarehouseReceiptResponse;
import com.agri_supplies_shop.entity.WarehouseReceipt;
import com.agri_supplies_shop.enums.PaymentStatus;
import com.agri_supplies_shop.enums.TypeStock;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.SupplierRepository;
import com.agri_supplies_shop.repository.WarehouseReceiptRepository;
import com.agri_supplies_shop.service.ReceiptDetailService;
import com.agri_supplies_shop.service.PaymentSlipService;
import com.agri_supplies_shop.service.WarehouseReceiptService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WarehouseReceiptServiceImpl implements WarehouseReceiptService {

    WarehouseReceiptRepository warehouseReceiptRepository;

    SupplierRepository supplierRepository;

    ReceiptDetailService receiptDetailService;

    PaymentSlipService supplierDebtService;

    WarehouseReceiptConverter warehouseReceiptConverter;


    @Override
    @Transactional
    public WarehouseReceiptResponse create(WarehouseReceiptRequest request) {
        WarehouseReceipt warehouseReceipt = WarehouseReceipt.builder()
                .createdDate(ZonedDateTime.now())
                .amount(request.getAmount())
                .note(request.getNote())
                .supplier(supplierRepository.findById(request.getSupplierId()).orElseThrow(
                        () -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND)
                ))
                .build();

        warehouseReceiptRepository.save(warehouseReceipt);
        List<ReceiptDetailResponse> receiptDetails = request.getReceiptDetails().stream().map(
                it -> receiptDetailService.create(it, warehouseReceipt.getId())
        ).toList();
        if (request.getAmount() != request.getMoneyForSupplier()) {
            PaymentSlipRequest supplierDebtRequest = PaymentSlipRequest.builder()
                    .type(TypeStock.RECEIPT.getName())
                    .warehouseReceiptId(warehouseReceipt.getId())
                    .paid(request.getMoneyForSupplier())
                    .outstandingDebt(request.getAmount() - request.getMoneyForSupplier())
                    .createdDate(ZonedDateTime.now())
                    .typeCode(0L)
                    .paymentMethod(request.getPaymentMethod())
                    .note("")
                    .build();
            supplierDebtService.create(supplierDebtRequest);
            warehouseReceipt.setPaymentStatus(PaymentStatus.NO_PAYMENT);
            warehouseReceiptRepository.save(warehouseReceipt);
        } else {
            warehouseReceipt.setPaymentStatus(PaymentStatus.PAID);
            warehouseReceiptRepository.save(warehouseReceipt);
        }


        WarehouseReceiptResponse response = warehouseReceiptConverter.toResponse(warehouseReceipt);
        response.setReceiptDetails(receiptDetails);
        return response;
    }

    @Override
    public PageResponse<WarehouseReceiptResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<WarehouseReceipt> pageData = warehouseReceiptRepository.findAll(pageable);
        return PageResponse.<WarehouseReceiptResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .totalPage(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .data(pageData.getContent().stream().map(
                        it -> warehouseReceiptConverter.toResponse(it)
                ).toList())
                .build();
    }
}

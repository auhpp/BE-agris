package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.WarehouseReceiptConverter;
import com.agri_supplies_shop.dto.request.PaymentSlipRequest;
import com.agri_supplies_shop.dto.request.WarehouseReceiptRequest;
import com.agri_supplies_shop.dto.request.WarehouseReceiptSearchRequest;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.dto.response.ReceiptDetailResponse;
import com.agri_supplies_shop.dto.response.WarehouseReceiptResponse;
import com.agri_supplies_shop.entity.PayeeType;
import com.agri_supplies_shop.entity.ReceiptDetail;
import com.agri_supplies_shop.entity.Supplier;
import com.agri_supplies_shop.entity.WarehouseReceipt;
import com.agri_supplies_shop.enums.ImportGoodsStatus;
import com.agri_supplies_shop.enums.PayeeTypeEnum;
import com.agri_supplies_shop.enums.PaymentStatus;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.*;
import com.agri_supplies_shop.repository.specification.BaseSpecification;
import com.agri_supplies_shop.repository.specification.SearchCriteria;
import com.agri_supplies_shop.service.PaymentSlipService;
import com.agri_supplies_shop.service.ReceiptDetailService;
import com.agri_supplies_shop.service.WarehouseReceiptService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WarehouseReceiptServiceImpl implements WarehouseReceiptService {

    WarehouseReceiptRepository warehouseReceiptRepository;

    SupplierRepository supplierRepository;

    ReceiptDetailService receiptDetailService;

    WarehouseReceiptConverter warehouseReceiptConverter;

    WarehouseRepository warehouseRepository;

    StaffRepository staffRepository;

    PaymentSlipService paymentSlipService;

    PayeeTypeRepository payeeTypeRepository;

    @Override
    @Transactional
    public WarehouseReceiptResponse create(WarehouseReceiptRequest request) {
        Supplier supplier = supplierRepository.findById(request.getSupplierId()).orElseThrow(
                () -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND)
        );
        //Warehouse
        WarehouseReceipt warehouseReceipt = WarehouseReceipt.builder()
                .createdDate(ZonedDateTime.now())
                .note(request.getNote())
                .supplier(supplier)
                .warehouse(warehouseRepository.findById(request.getWarehouseId()).orElseThrow(
                        () -> new AppException(ErrorCode.WAREHOUSE_NOT_EXISTED)
                ))
                .importStatus(ImportGoodsStatus.WAITING_FOR_IMPORT)
                .staff(staffRepository.findById(request.getStaffId()).orElseThrow(
                        () -> new AppException(ErrorCode.STAFF_NOT_EXISTED)
                ))
                .paid(request.getPaid())
                .amount(request.getAmount())
                .build();

        warehouseReceiptRepository.save(warehouseReceipt);
        //receipt detail
        List<ReceiptDetailResponse> receiptDetails = request.getReceiptDetails().stream().map(
                it -> receiptDetailService.create(it, warehouseReceipt.getId())
        ).toList();


        //Payment slip
        Long amount = request.getAmount();
        supplier.setDebt(supplier.getDebt() + amount);
        if (!Objects.equals(amount, request.getPaid())) {
            warehouseReceipt.setPaymentStatus(PaymentStatus.NO_PAYMENT);
        } else {
            warehouseReceipt.setPaymentStatus(PaymentStatus.PAID);
        }
        warehouseReceiptRepository.save(warehouseReceipt);
        if (request.getPaid() != 0) {
            PayeeType payeeType = payeeTypeRepository.findByName(PayeeTypeEnum.SUPPLIER);
            paymentSlipService.create(
                    PaymentSlipRequest.builder()
                            .paid(request.getPaid())
                            .debt(true)
                            .payeeId(supplier.getId())
                            .payeeTypeId(payeeType.getId())
                            .note("")
                            .paymentMethod(request.getPaymentMethod())
                            .paymentReasonId(null)
                            .reason("Thanh toán cho nhà cung cấp")
                            .payeeName("")
                            .build()
            );
        }
        WarehouseReceiptResponse response = warehouseReceiptConverter.toResponse(warehouseReceipt);
        response.setReceiptDetails(receiptDetails);
        return response;
    }

    @Override
    public PageResponse<WarehouseReceiptResponse> search(WarehouseReceiptSearchRequest request,
                                                         int page, int size) {
        Specification<WarehouseReceipt> spec = Specification.where(
                (root, query, criteriaBuilder) ->
                        criteriaBuilder.conjunction()
        );
        if (request.getSupplierId() != null) {
            BaseSpecification specSupplierId = new BaseSpecification(
                    SearchCriteria.builder()
                            .value(request.getSupplierId())
                            .nameObject("supplier")
                            .key("id")
                            .operation("=")
                            .build()
            );
            spec = spec.and(specSupplierId);
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<WarehouseReceipt> pageData = warehouseReceiptRepository.findAll(spec, pageable);
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

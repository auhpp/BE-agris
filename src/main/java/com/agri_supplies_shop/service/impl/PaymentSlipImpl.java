package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.PaymentSlipConverter;
import com.agri_supplies_shop.dto.request.PaymentSlipRequest;
import com.agri_supplies_shop.dto.request.PaymentSlipSearchRequest;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.dto.response.PayeeTypeResponse;
import com.agri_supplies_shop.dto.response.PaymentSlipResponse;
import com.agri_supplies_shop.entity.PayeeType;
import com.agri_supplies_shop.entity.PaymentReason;
import com.agri_supplies_shop.entity.PaymentSlip;
import com.agri_supplies_shop.enums.PayeeTypeEnum;
import com.agri_supplies_shop.enums.PaymentMethod;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.PayeeTypeRepository;
import com.agri_supplies_shop.repository.PaymentReasonRepository;
import com.agri_supplies_shop.repository.PaymentSlipRepository;
import com.agri_supplies_shop.repository.specification.BaseSpecification;
import com.agri_supplies_shop.repository.specification.SearchCriteria;
import com.agri_supplies_shop.service.PaymentSlipService;
import com.agri_supplies_shop.service.SupplierService;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentSlipImpl implements PaymentSlipService {

    PaymentReasonRepository paymentReasonRepository;
    PayeeTypeRepository payeeTypeRepository;
    PaymentSlipConverter paymentSlipConverter;
    SupplierService supplierService;
    PaymentSlipRepository paymentSlipRepository;

    @Override
    @Transactional
    public PaymentSlipResponse create(PaymentSlipRequest request) {
        PaymentReason paymentReason;
        PaymentSlip paymentSlip = new PaymentSlip();

        if (request.getPaymentReasonId() != null) {
            paymentReason = paymentReasonRepository.findById(request.getPaymentReasonId()).orElseThrow(
                    () -> new AppException(ErrorCode.PAYMENT_REASON_NOT_EXISTED)
            );
        } else {
            paymentReason = paymentReasonRepository.findByName(request.getReason());
            if (paymentReason == null) {
                paymentReason = PaymentReason.builder()
                        .name(request.getReason())
                        .build();
                paymentReasonRepository.save(paymentReason);
            }
        }
        paymentSlip.setPaymentReason(paymentReason);
        PayeeType payeeType = payeeTypeRepository.findById(request.getPayeeTypeId()).orElseThrow(
                () -> new AppException(ErrorCode.PAYMENT_REASON_NOT_EXISTED)
        );
        paymentSlip.setCreatedDate(ZonedDateTime.now());
        paymentSlip.setNote(request.getNote());
        paymentSlip.setPaid(request.getPaid());
        if (request.getPaymentMethod().equals(PaymentMethod.TRANSFER.name())) {
            paymentSlip.setPaymentMethod(PaymentMethod.TRANSFER);
        } else {
            paymentSlip.setPaymentMethod(PaymentMethod.CASH);
        }
        paymentSlip.setPayeeId(request.getPayeeId());
        if (request.getPayeeId() == null) {
            paymentSlip.setPayeeName(request.getPayeeName());
        } else {
            paymentSlip.setPayeeName(null);
        }
        paymentSlip.setPayeeType(payeeType);
        paymentSlip.setDebt(request.isDebt());
        if (request.isDebt() && request.getPayeeId() != null && payeeType.getName().equals(PayeeTypeEnum.SUPPLIER)) {
            supplierService.updateDebt(request.getPaid(), request.getPayeeId());
        }
        paymentSlipRepository.save(paymentSlip);
        return paymentSlipConverter.toResponse(paymentSlip);
    }

    @Override
    public PageResponse<PaymentSlipResponse> search(PaymentSlipSearchRequest request, int page, int size) {
        Specification<PaymentSlip> spec = Specification.where(
                (root, query, criteriaBuilder) ->
                        criteriaBuilder.conjunction()
        );
        if (!Objects.equals(request.getId(), null)) {
            BaseSpecification<PaymentSlip> specId = new BaseSpecification<>(
                    SearchCriteria.builder()
                            .operation("=")
                            .key("id")
                            .value(request.getId())
                            .build()
            );
            spec = spec.and(specId);
        }
        if (!Objects.equals(request.getPaymentReasonId(), null)) {
            BaseSpecification<PaymentSlip> specPaymentReasonId = new BaseSpecification<>(
                    SearchCriteria.builder()
                            .operation("=")
                            .key("id")
                            .nameObject("paymentReason")
                            .value(request.getPaymentReasonId())
                            .build()
            );
            spec = spec.and(specPaymentReasonId);
        }
        if (!Objects.equals(request.getPayeeTypeId(), null)) {
            BaseSpecification<PaymentSlip> specPayeeId = new BaseSpecification<>(
                    SearchCriteria.builder()
                            .operation("=")
                            .key("id")
                            .nameObject("payeeType")
                            .value(request.getPayeeTypeId())
                            .build()
            );
            spec = spec.and(specPayeeId);
        }
        if (!Objects.equals(request.getPayeeId(), null)) {
            BaseSpecification<PaymentSlip> specPayeeId = new BaseSpecification<>(
                    SearchCriteria.builder()
                            .operation("=")
                            .key("payeeId")
                            .value(request.getPayeeId())
                            .build()
            );
            spec = spec.and(specPayeeId);
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<PaymentSlip> pageData = paymentSlipRepository.findAll(spec, pageable);
        List<PaymentSlipResponse> paymentSlipResponses = pageData.stream().map(
                paymentSlipConverter::toResponse
        ).collect(Collectors.toList());
        Comparator<PaymentSlipResponse> comparatorDes = (s1, s2) -> s2.getCreatedDate()
                .compareTo(s1.getCreatedDate());
        Collections.sort(paymentSlipResponses, comparatorDes);
        return PageResponse.<PaymentSlipResponse>builder()
                .data(
                        paymentSlipResponses
                )
                .pageSize(pageData.getSize())
                .totalPage(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .currentPage(page)
                .build();
    }

    @Override
    public List<PayeeTypeResponse> getAllPayeeType() {
        List<PayeeType> payeeTypes = payeeTypeRepository.findAll();
        if (!payeeTypes.isEmpty()) {
            return payeeTypes.stream().map(
                    it -> PayeeTypeResponse.builder()
                            .name(it.getName().name())
                            .id(it.getId())
                            .build()
            ).toList();
        }
        return List.of();
    }
}

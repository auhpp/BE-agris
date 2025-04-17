package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.SupplierConverter;
import com.agri_supplies_shop.dto.request.SupplierRequest;
import com.agri_supplies_shop.dto.request.SupplierSearchRequest;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.dto.response.SupplierResponse;
import com.agri_supplies_shop.entity.Supplier;
import com.agri_supplies_shop.enums.Status;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.SupplierRepository;
import com.agri_supplies_shop.repository.specification.BaseSpecification;
import com.agri_supplies_shop.repository.specification.SearchCriteria;
import com.agri_supplies_shop.service.ProductService;
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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SupplierServiceImpl implements SupplierService {

    SupplierRepository supplierRepository;

    SupplierConverter supplierConverter;

    ProductService productService;

    @Override
    public SupplierResponse createSupplier(SupplierRequest request) {
        Supplier supplier;
        if (request.getId() != null) {
            supplier = supplierRepository.findById(request.getId()).orElseThrow(
                    () -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND)
            );
            if (!Objects.equals(supplier.getEmail(), request.getEmail())) {
                Supplier supplierValidate = supplierRepository.findByEmail(request.getEmail());
                if (supplierValidate != null) {
                    throw new AppException(ErrorCode.EMAIL_EXIT);
                }
            }
            if (!Objects.equals(supplier.getPhoneNumber(), request.getPhoneNumber())) {
                Supplier supplierValidate = supplierRepository.findByPhoneNumber(request.getPhoneNumber());
                if (supplierValidate != null) {
                    throw new AppException(ErrorCode.PHONE_NUMBER_EXIT);
                }
            }
            if (Objects.equals(request.getStatus(), "INACTIVE")) {
                supplier.setStatus(Status.INACTIVE);
            } else {
                supplier.setStatus(Status.ACTIVE);
            }
            supplier.setName(request.getName());
            supplier.setEmail(request.getEmail());
            supplier.setPhoneNumber(request.getPhoneNumber());
            supplier.setAddress(request.getAddress());
            supplier.setContactName(request.getContactName());
        } else {
            if (request.getEmail() != "") {
                Supplier supplierValidate = supplierRepository.findByEmail(request.getEmail());
                if (supplierValidate != null) {
                    throw new AppException(ErrorCode.EMAIL_EXIT);
                }
            }
            if (request.getPhoneNumber() != "") {
                Supplier supplierValidate = supplierRepository.findByPhoneNumber(request.getPhoneNumber());
                if (supplierValidate != null) {
                    throw new AppException(ErrorCode.PHONE_NUMBER_EXIT);
                }
            }
            supplier = supplierConverter.toEntity(request);
            supplier.setStatus(Status.ACTIVE);
        }
        supplierRepository.save(supplier);
        return supplierConverter.toResponse(supplier);
    }

    @Override
    public List<SupplierResponse> getAll() {
        return supplierRepository.findAll().stream().map(
                it -> supplierConverter.toResponse(it)
        ).toList();
    }

    @Override
    public List<SupplierResponse> find(String name, String phoneNumber) {
        if (Objects.equals(name, "")) {
            return List.of(supplierConverter.toResponse(supplierRepository.findByPhoneNumber(phoneNumber)));
        }
        if (Objects.equals(phoneNumber, "")) {
            return supplierRepository.findByNameContaining(name).stream().map(
                    it -> supplierConverter.toResponse(it)
            ).toList();
        }
        return List.of();
    }

    @Override
    public PageResponse<SupplierResponse> search(SupplierSearchRequest request, int page, int size) {
        BaseSpecification<Supplier> specName = new BaseSpecification<>(
                SearchCriteria.builder()
                        .operation(":")
                        .key("name")
                        .value(request.getName())
                        .build()
        );
        Specification<Supplier> spec = Specification.where(specName);
        if (!Objects.equals(request.getPhoneNumber(), "")) {
            BaseSpecification<Supplier> specPhoneNumber = new BaseSpecification<>(
                    SearchCriteria.builder()
                            .operation("=")
                            .key("phoneNumber")
                            .value(request.getPhoneNumber())
                            .build()
            );
            spec = spec.and(specPhoneNumber);
        }
        if (!Objects.equals(request.getId(), null)) {
            BaseSpecification<Supplier> specId = new BaseSpecification<>(
                    SearchCriteria.builder()
                            .operation("=")
                            .key("id")
                            .value(request.getId())
                            .build()
            );
            spec = spec.and(specId);
        }
        if (!Objects.equals(request.getEmail(), "")) {
            BaseSpecification<Supplier> specEmail = new BaseSpecification<>(
                    SearchCriteria.builder()
                            .operation("=")
                            .key("email")
                            .value(request.getEmail())
                            .build()
            );
            spec = spec.and(specEmail);
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Supplier> pageData = supplierRepository.findAll(spec, pageable);
        List<SupplierResponse> supplierResponses = pageData.stream().map(
                supplierConverter::toResponse
        ).collect(Collectors.toList());
        Comparator<SupplierResponse> comparatorDes = (s1, s2) -> s2.getDebt()
                .compareTo(s1.getDebt());
        Collections.sort(supplierResponses, comparatorDes);
        return PageResponse.<SupplierResponse>builder()
                .data(
                        supplierResponses
                )
                .pageSize(pageData.getSize())
                .totalPage(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .currentPage(page)
                .build();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND)
        );
        if (supplier.getWarehouseReceipts().isEmpty()) {
            supplierRepository.deleteById(id);
        } else {
            throw new AppException(ErrorCode.DELETE_FAILED);
        }
    }

    @Override
    @Transactional
    public void updateDebt(Long paid, Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND)
        );
        if (supplier.getDebt() != 0) {
            supplier.setDebt(supplier.getDebt() - paid);
            supplierRepository.save(supplier);
        } else {
            throw new AppException(ErrorCode.PAYMENT_SLIP_NOT_VALID);
        }
    }
}

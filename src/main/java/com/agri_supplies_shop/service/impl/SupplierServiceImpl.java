package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.SupplierConverter;
import com.agri_supplies_shop.dto.request.SupplierRequest;
import com.agri_supplies_shop.dto.response.SupplierResponse;
import com.agri_supplies_shop.entity.Supplier;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.SupplierRepository;
import com.agri_supplies_shop.service.ProductService;
import com.agri_supplies_shop.service.SupplierService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

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
            if (supplier.getEmail() != request.getEmail()) {
                Supplier supplierValidate = supplierRepository.findByEmail(request.getEmail());
                if (supplierValidate != null) {
                    throw new AppException(ErrorCode.EMAIL_EXIT);
                }
            }
            if (supplier.getPhoneNumber() != request.getPhoneNumber()) {
                Supplier supplierValidate = supplierRepository.findByPhoneNumber(request.getPhoneNumber());
                if (supplierValidate != null) {
                    throw new AppException(ErrorCode.PHONE_NUMBER_EXIT);
                }
            }
            supplier.setName(request.getName());
            supplier.setEmail(request.getEmail());
            supplier.setPhoneNumber(request.getPhoneNumber());
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
    @Transactional
    public void delete(List<Long> ids) {
        List<Supplier> suppliers = supplierRepository.findAllById(ids);
//        suppliers.forEach(
//                it -> {
//                    if (!it.getProducts().isEmpty()) {
//                        List<Long> productIds = it.getProducts().stream().map(
//                                product -> product.getId()
//                        ).toList();
//                        productService.delete(productIds);
//                    }
//                    supplierRepository.deleteById(it.getId());
//                }
//        );
    }
}

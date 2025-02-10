package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.SupplierConverter;
import com.agri_supplies_shop.dto.request.SupplierRequest;
import com.agri_supplies_shop.dto.response.SupplierResponse;
import com.agri_supplies_shop.entity.Supplier;
import com.agri_supplies_shop.repository.SupplierRepository;
import com.agri_supplies_shop.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private SupplierConverter supplierConverter;

    @Override
    public SupplierResponse createSupplier(SupplierRequest request) {
        Supplier supplier = supplierConverter.toEntity(request);
        supplierRepository.save(supplier);
        return supplierConverter.toResponse(supplier);
    }
}

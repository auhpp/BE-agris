package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.SupplierConverter;
import com.agri_supplies_shop.dto.request.SupplierRequest;
import com.agri_supplies_shop.dto.response.SupplierResponse;
import com.agri_supplies_shop.entity.Supplier;
import com.agri_supplies_shop.repository.SupplierRepository;
import com.agri_supplies_shop.service.ProductService;
import com.agri_supplies_shop.service.SupplierService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SupplierServiceImpl implements SupplierService {

    SupplierRepository supplierRepository;

    SupplierConverter supplierConverter;

    ProductService productService;

    @Override
    public SupplierResponse createSupplier(SupplierRequest request) {
        Supplier supplier = supplierConverter.toEntity(request);
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
    @Transactional
    public void delete(List<Long> ids) {
        List<Supplier> suppliers = supplierRepository.findAllById(ids);
        suppliers.forEach(
                it -> {
                    if (!it.getProducts().isEmpty()) {
                        List<Long> productIds = it.getProducts().stream().map(
                                product -> product.getId()
                        ).toList();
                        productService.delete(productIds);
                    }
                    supplierRepository.deleteById(it.getId());
                }
        );
    }
}

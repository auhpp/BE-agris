package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.SupplierRequest;
import com.agri_supplies_shop.dto.request.SupplierSearchRequest;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.dto.response.SupplierResponse;

import java.util.List;

public interface SupplierService {
    SupplierResponse createSupplier(SupplierRequest request);

    List<SupplierResponse> getAll();

    List<SupplierResponse> find(String name, String phoneNumber);

    PageResponse<SupplierResponse> search(SupplierSearchRequest request, int page, int size);

    void delete(Long id);

    void updateDebt(Long paid, Long supplierId);
}

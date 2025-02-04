package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.SupplierRequest;
import com.agri_supplies_shop.dto.response.SupplierResponse;

public interface SupplierService {
    SupplierResponse createSupplier(SupplierRequest request);
}

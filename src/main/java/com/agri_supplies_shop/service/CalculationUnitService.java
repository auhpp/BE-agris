package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.CalculationUnitRequest;
import com.agri_supplies_shop.dto.response.CalculationUnitResponse;

import java.util.List;

public interface CalculationUnitService {
    CalculationUnitResponse create(CalculationUnitRequest request);

    List<CalculationUnitResponse> getAll();
}

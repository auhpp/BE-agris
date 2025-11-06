package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.CalculationUnitRequest;
import com.agri_supplies_shop.dto.response.CalculationUnitResponse;
import com.agri_supplies_shop.dto.response.PageResponse;

import java.util.List;

public interface CalculationUnitService {
    CalculationUnitResponse create(CalculationUnitRequest request);

    List<CalculationUnitResponse> getAll();

    PageResponse<CalculationUnitResponse> search(String name, int page, int size);

    void delete(Long id);
}

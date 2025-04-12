package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.CalculationUnitConverter;
import com.agri_supplies_shop.dto.request.CalculationUnitRequest;
import com.agri_supplies_shop.dto.response.CalculationUnitResponse;
import com.agri_supplies_shop.entity.CalculationUnit;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.CalculationUnitRepository;
import com.agri_supplies_shop.service.CalculationUnitService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CalculationUnitServiceImpl implements CalculationUnitService {
    CalculationUnitRepository calculationUnitRepository;

    CalculationUnitConverter calculationUnitConverter;

    @Override
    @Transactional
    public CalculationUnitResponse create(CalculationUnitRequest request) {
        CalculationUnit calculationUnit;
        if (request.getId() != null) {
            calculationUnit = calculationUnitRepository.findById(request.getId()).orElseThrow(
                    () -> new AppException(ErrorCode.CALCULATION_NOT_EXISTED)
            );
            calculationUnitConverter.toExistedEntity(request, calculationUnit);
        } else {
            calculationUnit = calculationUnitConverter.toEntity(request);
        }
        return calculationUnitConverter.toResponse(
                calculationUnitRepository.save(calculationUnit)
        );
    }

    @Override
    public List<CalculationUnitResponse> getAll() {
        return calculationUnitRepository.findAll().stream().map(
                it -> calculationUnitConverter.toResponse(it)
        ).toList();
    }
}

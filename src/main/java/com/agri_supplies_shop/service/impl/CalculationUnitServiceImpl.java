package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.CalculationUnitConverter;
import com.agri_supplies_shop.dto.request.CalculationUnitRequest;
import com.agri_supplies_shop.dto.response.CalculationUnitResponse;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.entity.CalculationUnit;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.CalculationUnitRepository;
import com.agri_supplies_shop.service.CalculationUnitService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

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
            if (!Objects.equals(calculationUnit.getName(), request.getName())) {
                calculationUnit = calculationUnitRepository.findByName(request.getName());
                if (calculationUnit != null) {
                    throw new AppException(ErrorCode.CALCULATION_EXISTED);
                }
            }
            calculationUnitConverter.toExistedEntity(request, calculationUnit);
        } else {
            calculationUnit = calculationUnitRepository.findByName(request.getName());
            if (calculationUnit != null) {
                throw new AppException(ErrorCode.CALCULATION_EXISTED);
            }
            calculationUnit = calculationUnitConverter.toEntity(request);
        }
        return calculationUnitConverter.toResponse(
                calculationUnitRepository.save(calculationUnit)
        );
    }

    @Override
    public List<CalculationUnitResponse> getAll() {
        return calculationUnitRepository.findAll().stream().map(
                calculationUnitConverter::toResponse
        ).toList();
    }

    @Override
    public PageResponse<CalculationUnitResponse> search(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<CalculationUnit> pageData = calculationUnitRepository.findByNameContaining(name, pageable);
        return PageResponse.<CalculationUnitResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .totalPage(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .data(
                        pageData.stream().map(
                                calculationUnitConverter::toResponse
                        ).toList()
                )
                .build();
    }

    @Override
    public void delete(Long id) {
        CalculationUnit calculationUnit = calculationUnitRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.CALCULATION_NOT_EXISTED)
        );
        if (calculationUnit.getProductVariantValues().isEmpty()) {
            calculationUnitRepository.deleteById(id);
        } else {
            throw new AppException(ErrorCode.DELETE_FAILED);
        }
    }
}

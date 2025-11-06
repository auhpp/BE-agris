package com.agri_supplies_shop.converter;

import com.agri_supplies_shop.dto.request.CalculationUnitRequest;
import com.agri_supplies_shop.dto.response.CalculationUnitResponse;
import com.agri_supplies_shop.entity.CalculationUnit;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CalculationUnitConverter {
    ModelMapper modelMapper;

    public CalculationUnit toEntity(CalculationUnitRequest request) {
        return modelMapper.map(request, CalculationUnit.class);
    }

    public void toExistedEntity(CalculationUnitRequest request, CalculationUnit calculationUnit) {
        modelMapper.map(request, calculationUnit);
    }

    public CalculationUnitResponse toResponse(CalculationUnit calculationUnit) {
        return modelMapper.map(calculationUnit, CalculationUnitResponse.class);
    }
}

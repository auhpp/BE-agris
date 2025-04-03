package com.agri_supplies_shop.web.rest;

import com.agri_supplies_shop.dto.request.CalculationUnitRequest;
import com.agri_supplies_shop.dto.response.ApiResponse;
import com.agri_supplies_shop.dto.response.CalculationUnitResponse;
import com.agri_supplies_shop.service.CalculationUnitService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calculation_unit")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CalculationUnitResource {
    CalculationUnitService calculationUnitService;

    @PostMapping
    public ApiResponse create(@RequestBody CalculationUnitRequest request) {
        return ApiResponse.<CalculationUnitResponse>builder()
                .code(200)
                .result(calculationUnitService.create(request))
                .build();
    }

    @GetMapping
    public ApiResponse getAll() {
        return ApiResponse.<List<CalculationUnitResponse>>builder()
                .code(200)
                .result(calculationUnitService.getAll())
                .build();
    }
}

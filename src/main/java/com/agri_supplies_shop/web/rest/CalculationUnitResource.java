package com.agri_supplies_shop.web.rest;

import com.agri_supplies_shop.dto.request.CalculationUnitRequest;
import com.agri_supplies_shop.dto.response.ApiResponse;
import com.agri_supplies_shop.dto.response.CalculationUnitResponse;
import com.agri_supplies_shop.dto.response.PageResponse;
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

    @GetMapping("/search")
    public ApiResponse search(@RequestParam(name = "name",
                                      defaultValue = "") String name,
                              @RequestParam(name = "size", required = false,
                                      defaultValue = "10") int size,
                              @RequestParam(name = "page", required = false,
                                      defaultValue = "1") int page) {
        return ApiResponse.<PageResponse<CalculationUnitResponse>>builder()
                .code(200)
                .result(calculationUnitService.search(name, page, size))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse delete(@PathVariable("id") Long id) {
        calculationUnitService.delete(id);
        return ApiResponse.builder()
                .code(200)
                .build();
    }
}

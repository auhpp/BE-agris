package com.agri_supplies_shop.web.rest;

import com.agri_supplies_shop.dto.request.SupplierRequest;
import com.agri_supplies_shop.dto.response.ApiResponse;
import com.agri_supplies_shop.dto.response.SupplierResponse;
import com.agri_supplies_shop.service.SupplierService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplier")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SupplierResource {

    SupplierService supplierService;

    @PostMapping
    public ApiResponse<SupplierResponse> createSupplier(@RequestBody SupplierRequest request) {
        return ApiResponse.<SupplierResponse>builder()
                .code(200)
                .message("Create supplier success!")
                .result(supplierService.createSupplier(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<SupplierResponse>> getAll() {
        return ApiResponse.<List<SupplierResponse>>builder()
                .code(200)
                .result(supplierService.getAll())
                .build();
    }

    @DeleteMapping("/{ids}")
    public void getAll(@PathVariable("ids") List<Long> ids) {
        supplierService.delete(ids);
    }
}

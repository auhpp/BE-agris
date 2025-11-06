package com.agri_supplies_shop.web.rest;

import com.agri_supplies_shop.dto.request.SupplierRequest;
import com.agri_supplies_shop.dto.request.SupplierSearchRequest;
import com.agri_supplies_shop.dto.response.ApiResponse;
import com.agri_supplies_shop.dto.response.PageResponse;
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

    @GetMapping("/find")
    public ApiResponse<List<SupplierResponse>> find(@RequestParam(name = "name") String name,
                                                    @RequestParam(name = "phoneNumber") String phoneNumber
    ) {
        return ApiResponse.<List<SupplierResponse>>builder()
                .code(200)
                .result(supplierService.find(name, phoneNumber))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse delete(@PathVariable("id") Long id) {
        supplierService.delete(id);
        return ApiResponse.builder()
                .code(200)
                .build();
    }

    @GetMapping("/search")
    public ApiResponse search(SupplierSearchRequest request,
                              @RequestParam(name = "size", required = false,
                                      defaultValue = "10") int size,
                              @RequestParam(name = "page", required = false,
                                      defaultValue = "1") int page) {
        return ApiResponse.<PageResponse<SupplierResponse>>builder()
                .code(200)
                .result(supplierService.search(request, page, size))
                .build();
    }

}

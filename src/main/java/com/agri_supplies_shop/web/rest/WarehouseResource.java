package com.agri_supplies_shop.web.rest;

import com.agri_supplies_shop.dto.request.WarehouseReceiptRequest;
import com.agri_supplies_shop.dto.request.WarehouseRequest;
import com.agri_supplies_shop.dto.response.ApiResponse;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.dto.response.WarehouseReceiptResponse;
import com.agri_supplies_shop.dto.response.WarehouseResponse;
import com.agri_supplies_shop.service.WarehouseReceiptService;
import com.agri_supplies_shop.service.WarehouseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/warehouse")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WarehouseResource {

    WarehouseReceiptService warehouseReceiptService;

    WarehouseService warehouseService;

    @PostMapping
    public ApiResponse createWarehouse(@RequestBody WarehouseRequest request) {
        return ApiResponse.<WarehouseResponse>builder()
                .code(200)
                .result(warehouseService.create(request))
                .build();
    }

    @GetMapping
    public ApiResponse getAll(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<WarehouseResponse>>builder()
                .code(200)
                .result(warehouseService.search(name, page, size))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteWarehouse(@PathVariable("id") Long id) {
        return ApiResponse.<Boolean>builder()
                .code(200)
                .result(warehouseService.delete(id))
                .build();
    }

    @PostMapping("/receipt")
    public ApiResponse create(@RequestBody WarehouseReceiptRequest request) {
        return ApiResponse.<WarehouseReceiptResponse>builder()
                .code(200)
                .result(warehouseReceiptService.create(request))
                .build();
    }

    @GetMapping("/receipt")
    public ApiResponse getAll(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
                              @RequestParam(name = "size", required = false, defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<WarehouseReceiptResponse>>builder()
                .code(200)
                .result(warehouseReceiptService.getAll(page, size))
                .build();
    }

}

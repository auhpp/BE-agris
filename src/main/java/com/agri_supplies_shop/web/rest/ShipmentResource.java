package com.agri_supplies_shop.web.rest;

import com.agri_supplies_shop.dto.request.ShipmentSearchRequest;
import com.agri_supplies_shop.dto.response.ApiResponse;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.dto.response.ShipmentResponse;
import com.agri_supplies_shop.service.ShipmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shipment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShipmentResource {
    ShipmentService shipmentService;

    @GetMapping("/{id}")
    public ApiResponse<List<ShipmentResponse>> findByProductVariant(@PathVariable("id") Long id) {
        return ApiResponse.<List<ShipmentResponse>>builder()
                .result(shipmentService.findByProductVariantId(id))
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<PageResponse<ShipmentResponse>> search(ShipmentSearchRequest request,
                                                              @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                              @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<ShipmentResponse>>builder()
                .code(200)
                .result(shipmentService.search(request, page, size))
                .build();
    }
}

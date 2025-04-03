package com.agri_supplies_shop.web.rest;

import com.agri_supplies_shop.dto.response.ApiResponse;
import com.agri_supplies_shop.dto.response.ProductVariantValueResponse;
import com.agri_supplies_shop.service.ProductVariantValueService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/variant")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductVariantResource {
    ProductVariantValueService productVariantValueService;

    @GetMapping
    public ApiResponse<List<ProductVariantValueResponse>> search(
            @RequestParam("query") String name
    ) {
        return ApiResponse.<List<ProductVariantValueResponse>>
                        builder()
                .code(200)
                .result(productVariantValueService.search(name))
                .build();
    }
}

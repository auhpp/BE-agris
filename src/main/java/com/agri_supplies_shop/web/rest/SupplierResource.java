package com.agri_supplies_shop.web.rest;

import com.agri_supplies_shop.dto.request.SupplierRequest;
import com.agri_supplies_shop.dto.response.ApiResponse;
import com.agri_supplies_shop.dto.response.SupplierResponse;
import com.agri_supplies_shop.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/supplier")
public class SupplierResource {

    @Autowired
    private SupplierService supplierService;

    @PostMapping
    public ApiResponse<SupplierResponse> createSupplier(@RequestBody SupplierRequest request){
        return ApiResponse.<SupplierResponse>builder()
                .code(200)
                .message("Create supplier success!")
                .result(supplierService.createSupplier(request))
                .build();
    }
}

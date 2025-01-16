package com.agri_supplies_shop.web.rest;

import com.agri_supplies_shop.dto.request.ProductRequest;
import com.agri_supplies_shop.dto.response.ApiResponse;
import com.agri_supplies_shop.dto.response.ProductResponse;
import com.agri_supplies_shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductResource {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ApiResponse<ProductResponse> createAndUpdateProduct(@RequestBody ProductRequest productRequest) {
        ApiResponse<ProductResponse> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("Create product success!");
        response.setResult(productService.createAndUpdateProduct(productRequest));
        return response;
    }

}

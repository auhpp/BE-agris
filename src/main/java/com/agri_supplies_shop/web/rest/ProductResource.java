package com.agri_supplies_shop.web.rest;

import com.agri_supplies_shop.dto.request.ProductRequest;
import com.agri_supplies_shop.dto.response.ApiResponse;
import com.agri_supplies_shop.dto.response.ProductResponse;
import com.agri_supplies_shop.service.ProductService;
import com.agri_supplies_shop.service.ProductVariantValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductResource {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductVariantValueService productVariantValueService;

    @PostMapping
    public ApiResponse<ProductResponse> createAndUpdateProduct(@RequestBody ProductRequest productRequest) {
        ApiResponse<ProductResponse> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("Create product success!");
        response.setResult(productService.createAndUpdateProduct(productRequest));
        return response;
    }

    @DeleteMapping("/{ids}")
    public ApiResponse<String> deleteProduct(@PathVariable List<Long> ids) {
        ApiResponse<String> response = new ApiResponse<>();
        productService.deleteProduct(ids);
        response.setCode(200);
        response.setMessage("Delete product success!");
        response.setResult("Xóa sản phẩm thành công!");
        return response;
    }

    @DeleteMapping("/variant/{ids}")
    public ApiResponse<String> deleteProductVariant(@PathVariable List<Long> ids) {
        ApiResponse<String> response = new ApiResponse<>();
        productVariantValueService.deleteProductVariant(ids);
        response.setCode(200);
        response.setMessage("Delete product variant success!");
        response.setResult("Xóa biến thể sản phẩm thành công!");
        return response;
    }

}

package com.agri_supplies_shop.web.rest;

import com.agri_supplies_shop.dto.request.ProductRequest;
import com.agri_supplies_shop.dto.request.SearchProductRequest;
import com.agri_supplies_shop.dto.response.ApiResponse;
import com.agri_supplies_shop.dto.response.ImageResponse;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.dto.response.ProductResponse;
import com.agri_supplies_shop.service.AttributeService;
import com.agri_supplies_shop.service.ProductService;
import com.agri_supplies_shop.service.ProductVariantValueService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductResource {
    ProductService productService;

    ProductVariantValueService productVariantValueService;

    AttributeService attributeService;

    @PostMapping
    public ApiResponse<ProductResponse> createAndUpdate(
            @RequestBody ProductRequest productRequest
    ) {
        ApiResponse<ProductResponse> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("Create product success!");
        response.setResult(productService.createAndUpdate(productRequest));
        return response;
    }

    @GetMapping("/search")
    public ApiResponse<PageResponse<ProductResponse>> find(SearchProductRequest params,
                                                           @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                           @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        ApiResponse<PageResponse<ProductResponse>> response = new ApiResponse<>();
        response.setCode(200);

        response.setResult(productService.find(params, page, size));
        return response;
    }

    @GetMapping("/get/{id}")
    public ApiResponse<ProductResponse> findById(@PathVariable("id") Long id) {
        ApiResponse<ProductResponse> response = new ApiResponse<>();
        response.setCode(200);
        response.setResult(productService.findById(id));
        return response;
    }

    @DeleteMapping("/{ids}")
    public ApiResponse<String> delete(@PathVariable List<Long> ids) {
        ApiResponse<String> response = new ApiResponse<>();
        productService.delete(ids);
        response.setCode(200);
        response.setResult("Delete product success!");
        return response;
    }

    @DeleteMapping("/variant/{ids}")
    public ApiResponse<String> deleteProductVariant(@PathVariable List<Long> ids) {
        ApiResponse<String> response = new ApiResponse<>();
        productVariantValueService.delete(ids);
        response.setCode(200);
        response.setResult("Delete product variant success!");
        return response;
    }

    @DeleteMapping("/attribute/{id}")
    public ApiResponse<String> deleteAttribute(@PathVariable("id") Long id) {
        ApiResponse<String> response = new ApiResponse<>();
        attributeService.deleteById(id);
        response.setCode(200);
        response.setResult("Delete attribute success!");
        return response;
    }

    @PostMapping("/thumbnail")
    public ApiResponse<ImageResponse> uploadThumbnail(@RequestParam("file") MultipartFile file, @RequestParam("productId") Long productId) throws IOException {
        ApiResponse<ImageResponse> response = new ApiResponse<>();

        response.setCode(200);
        response.setResult(productService.uploadThumbnail(file, productId));
        return response;
    }

    @PostMapping("/images")
    public ApiResponse<List<ImageResponse>> uploadImages(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("productId") Long productId) throws IOException {
        ApiResponse<List<ImageResponse>> response = new ApiResponse<>();

        response.setCode(200);
        response.setResult(productService.uploadImages(files, productId));
        return response;
    }
}

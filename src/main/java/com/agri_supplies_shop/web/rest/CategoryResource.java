package com.agri_supplies_shop.web.rest;

import com.agri_supplies_shop.dto.request.CategoryRequest;
import com.agri_supplies_shop.dto.response.ApiResponse;
import com.agri_supplies_shop.dto.response.CategoryResponse;
import com.agri_supplies_shop.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryResource {

    CategoryService categoryService;

    @PostMapping
    public ApiResponse<CategoryResponse> createCategory(@RequestBody CategoryRequest request) {
        return ApiResponse.<CategoryResponse>builder()
                .code(200)
                .message("Create category success!")
                .result(
                        categoryService.createCategory(request)
                ).build();
    }

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .code(200)
                .message("Get all categories success!")
                .result(
                        categoryService.getAllCategories()
                )
                .build();
    }

    @DeleteMapping("/{id}")
    public void getAllCategories(@PathVariable("id") Long id) {
        categoryService.delete(id);
    }
}

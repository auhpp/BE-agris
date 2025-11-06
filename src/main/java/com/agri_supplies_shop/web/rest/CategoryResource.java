package com.agri_supplies_shop.web.rest;

import com.agri_supplies_shop.dto.request.CategoryRequest;
import com.agri_supplies_shop.dto.response.ApiResponse;
import com.agri_supplies_shop.dto.response.CategoryResponse;
import com.agri_supplies_shop.dto.response.PageResponse;
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

    @GetMapping("/search")
    public ApiResponse<PageResponse<CategoryResponse>> search(@RequestParam(name = "name",
                                                                      defaultValue = "") String name,
                                                              @RequestParam(name = "size", required = false,
                                                                      defaultValue = "10") int size,
                                                              @RequestParam(name = "page", required = false,
                                                                      defaultValue = "1") int page
    ) {
        return ApiResponse.<PageResponse<CategoryResponse>>builder()
                .code(200)
                .result(
                        categoryService.search(name, page, size)
                )
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteCategory(@PathVariable("id") Long id) {
        categoryService.delete(id);
        return ApiResponse.builder()
                .code(200)
                .build();
    }
}

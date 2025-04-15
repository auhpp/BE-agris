package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.CategoryRequest;
import com.agri_supplies_shop.dto.response.CategoryResponse;
import com.agri_supplies_shop.dto.response.PageResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest categoryRequest);

    List<CategoryResponse> getAllCategories();

    void delete(Long id);

    PageResponse<CategoryResponse> search(String name, int page, int size);
}

package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.CategoryConverter;
import com.agri_supplies_shop.dto.request.CategoryRequest;
import com.agri_supplies_shop.dto.response.CategoryResponse;
import com.agri_supplies_shop.entity.Category;
import com.agri_supplies_shop.enums.Status;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.CategoryRepository;
import com.agri_supplies_shop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryConverter categoryConverter;

    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        Category parent = new Category();
        if (categoryRequest.getParentId() != null)
            parent = categoryRepository.findById(categoryRequest.getParentId()).orElseThrow(
                    () -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)
            );
        else parent = null;
        Category category = Category
                .builder()
                .name(categoryRequest.getName())
                .status(Status.ACTIVE)
                .parent(parent)
                .build();
        categoryRepository.save(category);
        return categoryConverter.toCategoryResponse(category);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        Map<Long, CategoryResponse> categoryMap = categories.stream()
                .collect(Collectors.toMap(
                        category -> category.getId(),
                        category -> categoryConverter.toCategoryResponse(category)
                ));
        List<CategoryResponse> rootCategories = new ArrayList<>();


        for (Category category : categories) {
            CategoryResponse response = categoryMap.get(category.getId());
            if (category.getParent() != null) {
                CategoryResponse parentResponse = categoryMap.get(category.getParent().getId());
                if (parentResponse != null) {
                    parentResponse.getChildCategory().add(response);
                }
            } else {
                rootCategories.add(response);
            }
        }
        return rootCategories;
    }

}

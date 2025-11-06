package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.CategoryConverter;
import com.agri_supplies_shop.dto.request.CategoryRequest;
import com.agri_supplies_shop.dto.response.CategoryResponse;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.entity.Category;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.CategoryRepository;
import com.agri_supplies_shop.service.CategoryService;
import com.agri_supplies_shop.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;

    CategoryConverter categoryConverter;

    ProductService productService;

    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        Category category;
        category = categoryRepository.findByName(categoryRequest.getName().trim());
        if (category != null) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }
        if (categoryRequest.getId() != null) {
            category = categoryRepository.findById(categoryRequest.getId()).orElseThrow(
                    () -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)
            );
            category.setName(categoryRequest.getName());
        } else {
            category = Category
                    .builder()
                    .name(categoryRequest.getName())
                    .build();
        }
        categoryRepository.save(category);
        return categoryConverter.toCategoryResponse(category);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        //Get all category
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponse> categoryResponses = categories.stream().map(
                it ->
                        categoryConverter.toCategoryResponse(it)
        ).toList();
        return categoryResponses;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)
        );
        if (!category.getProducts().isEmpty()) {
            throw new AppException(ErrorCode.DELETE_FAILED);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public PageResponse<CategoryResponse> search(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Category> categories = categoryRepository.findByNameContaining(name, pageable);
        return PageResponse.<CategoryResponse>builder()
                .data(categories.stream().map(
                        categoryConverter::toCategoryResponse
                ).toList())
                .pageSize(categories.getSize())
                .totalPage(categories.getTotalPages())
                .totalElements(categories.getTotalElements())
                .currentPage(page)
                .build();
    }

}

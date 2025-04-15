package com.agri_supplies_shop.converter;

import com.agri_supplies_shop.dto.response.CategoryResponse;
import com.agri_supplies_shop.entity.Category;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryConverter {
    ModelMapper modelMapper;

    public CategoryResponse toCategoryResponse(Category category) {
        return modelMapper.map(category, CategoryResponse.class);
    }
}
    
package com.agri_supplies_shop.converter;

import com.agri_supplies_shop.dto.response.CategoryResponse;
import com.agri_supplies_shop.entity.Category;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter {
    @Autowired
    private ModelMapper modelMapper;

    public CategoryResponse toCategoryResponse(Category category){
        return modelMapper.map(category, CategoryResponse.class);
    }
}

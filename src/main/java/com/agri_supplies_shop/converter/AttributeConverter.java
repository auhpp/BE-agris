package com.agri_supplies_shop.converter;

import com.agri_supplies_shop.dto.response.AttributeResponse;
import com.agri_supplies_shop.entity.ProductAttributeValue;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AttributeConverter {
    @Autowired
    private ModelMapper modelMapper;

    public AttributeResponse toAttributeResponse(ProductAttributeValue attribute){
        AttributeResponse response = modelMapper.map(attribute, AttributeResponse.class);
        response.setName(attribute.getAttribute().getName());
        return response;
    }
}

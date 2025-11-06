package com.agri_supplies_shop.converter;

import com.agri_supplies_shop.dto.response.AttributeResponse;
import com.agri_supplies_shop.entity.ProductAttributeValue;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttributeConverter {

    ModelMapper modelMapper;

    public AttributeResponse toResponse(ProductAttributeValue attribute) {
        AttributeResponse response = modelMapper.map(attribute, AttributeResponse.class);
        response.setName(attribute.getAttribute().getName());
        return response;
    }
}

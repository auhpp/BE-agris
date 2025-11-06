package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.AttributeConverter;
import com.agri_supplies_shop.dto.request.AttributeRequest;
import com.agri_supplies_shop.dto.response.AttributeResponse;
import com.agri_supplies_shop.entity.Attribute;
import com.agri_supplies_shop.entity.ProductAttributeValue;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.AttributeRepository;
import com.agri_supplies_shop.repository.AttributeValueRepository;
import com.agri_supplies_shop.repository.ProductRepository;
import com.agri_supplies_shop.service.AttributeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttributeServiceImpl implements AttributeService {

    AttributeRepository attributeRepository;

    AttributeValueRepository attributeValueRepository;

    AttributeConverter attributeConverter;

    ProductRepository productRepository;

    @Override
    @Transactional
    public AttributeResponse create(AttributeRequest request, Long productId) {
        //Attribute
        Attribute attribute = attributeRepository.findByName(request.getName());
        if (attribute == null) {
            attribute = Attribute.builder().name(request.getName()).build();
            //Save attribute
            attributeRepository.save(attribute);
        }
        //Product attribute value
        ProductAttributeValue pAttributeVal = new ProductAttributeValue();
        if (request.getId() != null) {
            pAttributeVal = attributeValueRepository.findById(request.getId()).orElseThrow(
                    () -> new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND)
            );
        }
        pAttributeVal.setValue(request.getValue());
        pAttributeVal.setAttribute(attribute);
        pAttributeVal.setProduct(productRepository.findById(productId).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)
        ));

        //Save product attribute value
        return attributeConverter.toResponse(attributeValueRepository.save(pAttributeVal));

    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        attributeValueRepository.deleteById(id);
    }
}

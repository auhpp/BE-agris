package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.AttributeConverter;
import com.agri_supplies_shop.dto.request.AttributeRequest;
import com.agri_supplies_shop.dto.response.AttributeResponse;
import com.agri_supplies_shop.entity.Attribute;
import com.agri_supplies_shop.entity.Product;
import com.agri_supplies_shop.entity.ProductAttributeValue;
import com.agri_supplies_shop.repository.AttributeRepository;
import com.agri_supplies_shop.repository.AttributeValueRepository;
import com.agri_supplies_shop.service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AttributeServiceImpl implements AttributeService {

    @Autowired
    private AttributeRepository attributeRepository;

    @Autowired
    private AttributeValueRepository attributeValueRepository;

    @Autowired
    private AttributeConverter attributeConverter;

    @Override
    @Transactional
    public AttributeResponse createAttribute(AttributeRequest attributeRequest, Product product) {
        //Attribute
        Attribute attribute = attributeRepository.findByName(attributeRequest.getName());
        if (attribute == null) {
            attribute = Attribute.builder().name(attributeRequest.getName()).build();
            //Save attribute
            attributeRepository.save(attribute);
        }
        //Product attribute value
        ProductAttributeValue pAttributeVal = ProductAttributeValue
                .builder()
                .value(attributeRequest.getValue())
                .attribute(attribute)
                .product(product)
                .build();
        //Save product attribute value
        attributeValueRepository.save(pAttributeVal);
        return attributeConverter.toAttributeResponse(pAttributeVal);

    }
}

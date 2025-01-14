package com.agri_supplies_shop.converter;

import com.agri_supplies_shop.dto.request.ProductPriceRequest;
import com.agri_supplies_shop.entity.ProductPrice;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductPriceConverter {
    @Autowired
    private ModelMapper modelMapper;

    public ProductPrice toProductPriceEntity(ProductPriceRequest productPriceRequest){
        return modelMapper.map(productPriceRequest, ProductPrice.class);
    }
}

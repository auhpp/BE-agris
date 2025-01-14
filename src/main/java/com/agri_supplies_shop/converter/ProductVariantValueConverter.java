package com.agri_supplies_shop.converter;

import com.agri_supplies_shop.dto.response.ProductPriceResponse;
import com.agri_supplies_shop.dto.response.ProductVariantValueResponse;
import com.agri_supplies_shop.dto.response.VariantResponse;
import com.agri_supplies_shop.entity.ProductPrice;
import com.agri_supplies_shop.entity.ProductVariantValue;
import com.agri_supplies_shop.enums.Status;
import com.agri_supplies_shop.repository.VariantValueRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ProductVariantValueConverter {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private VariantValueRepository variantValueRepository;

    public ProductVariantValueResponse toProductVariantValueResponse(ProductVariantValue request) {
        ProductVariantValueResponse response = modelMapper.map(request, ProductVariantValueResponse.class);
        //Handle product price
        ProductPriceResponse price = new ProductPriceResponse();
        for (ProductPrice it : request.getProductPrices()) {
            if (it.getStatus() == Status.ACTIVE) {
                price.setId(it.getId());
                Long priceNum = it.getPrice();
                Long oldPrice = null;
                String discount = null;
                if (it.getDiscount() != null) {
                    oldPrice = priceNum;
                    if (it.getDiscountUnit().equals("%")) {
                        priceNum = (long) (priceNum - priceNum * (((float) it.getDiscount()) / 100));
                    } else if (it.getDiscountUnit().equals("đ")) {
                        priceNum = priceNum - it.getDiscount();
                    }

                    discount = it.getDiscount() + it.getDiscountUnit();
                }
                price.setPrice(priceNum);
                price.setOldPrice(oldPrice);
                price.setDiscount(discount);
            }
        }
        response.setPrice(price);
        //Sku
        List<Long> variantIds = Arrays.stream(request.getSku().split("[-]")).map(
                it -> Long.parseLong(it)
        ).toList();
        //Get variant
        List<VariantResponse> variants = variantValueRepository.findAllById(variantIds).stream().map(
                it -> VariantResponse
                        .builder()
                        .id(it.getId())
                        .name(it.getVariant().getName())
                        .value(it.getValue())
                        .build()
        ).toList();
        response.setVariantValues(variants);
        return response;
    }
}

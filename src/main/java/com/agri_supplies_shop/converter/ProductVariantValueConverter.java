package com.agri_supplies_shop.converter;

import com.agri_supplies_shop.dto.request.VariantRequest;
import com.agri_supplies_shop.dto.request.VariantValueRequest;
import com.agri_supplies_shop.dto.response.ProductVariantValueResponse;
import com.agri_supplies_shop.dto.response.VariantResponse;
import com.agri_supplies_shop.entity.ProductVariantValue;
import com.agri_supplies_shop.entity.VariantValue;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
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

    public ProductVariantValue toProductVariantValueEntity(VariantValueRequest request){
        return modelMapper.map(request, ProductVariantValue.class);
    }
    public ProductVariantValue fromRequestToProductVariantValueEntity(VariantValueRequest request, ProductVariantValue variantValue){
        modelMapper.map(request, variantValue);
        return variantValue;
    }
    public ProductVariantValueResponse toProductVariantValueResponse(ProductVariantValue request) {
        ProductVariantValueResponse response = modelMapper.map(request, ProductVariantValueResponse.class);
        Long priceNum = request.getPrice();
        Long oldPrice = null;
        String discount = null;
        if (request.getDiscount() != null) {
            oldPrice = priceNum;
            if (request.getDiscountUnit().equals("%")) {
                priceNum = (long) (priceNum - priceNum * (((float) request.getDiscount()) / 100));
            } else if (request.getDiscountUnit().equals("đ")) {
                priceNum = priceNum - request.getDiscount();
            }

            discount = request.getDiscount() + request.getDiscountUnit();
        }
        response.setPrice(priceNum);
        response.setOldPrice(oldPrice);
        response.setDiscount(discount);

        //Sku
        List<Long> variantIds = Arrays.stream(request.getSku().split("[-]")).map(
                it -> Long.parseLong(it)
        ).toList();
        //Get variant
        List<VariantValue> variants = variantIds.stream().map(
                it ->
                        variantValueRepository.findById(it).orElseThrow(
                                () -> new AppException(ErrorCode.VARIANT_VALUE_NOT_FOUND)
                        )
        ).toList();
        List<VariantResponse> variantResponses = variants.stream().map(
                it -> VariantResponse
                        .builder()
                        .id(it.getId())
                        .name(it.getVariant().getName())
                        .value(it.getValue())
                        .build()
        ).toList();
        response.setVariantValues(variantResponses);
        return response;
    }
}

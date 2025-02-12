package com.agri_supplies_shop.converter;

import com.agri_supplies_shop.dto.request.CartItemRequest;
import com.agri_supplies_shop.dto.response.CartItemResponse;
import com.agri_supplies_shop.entity.CartItem;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartConverter {
    ModelMapper modelMapper;

    ProductConverter productConverter;

    public CartItem toEntity(CartItemRequest request) {
        return modelMapper.map(request, CartItem.class);
    }

    public void toExistsEntity(CartItemRequest request, CartItem cartItem) {
        modelMapper.map(request, cartItem);
    }

    public CartItemResponse toResponse(CartItem cartItem) {
        CartItemResponse response = modelMapper.map(cartItem, CartItemResponse.class);
        response.setProductVariantId(cartItem.getProductVariantValue().getId());
        response.setProduct(productConverter.toResponse(cartItem.getProductVariantValue().getProduct()));
        return response;
    }
}

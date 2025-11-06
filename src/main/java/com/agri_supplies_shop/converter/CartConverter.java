package com.agri_supplies_shop.converter;

import com.agri_supplies_shop.dto.request.CartItemRequest;
import com.agri_supplies_shop.dto.response.CartItemResponse;
import com.agri_supplies_shop.entity.Cart;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartConverter {
    ModelMapper modelMapper;

    ProductConverter productConverter;

    ProductVariantValueConverter variantConverter;

    public Cart toEntity(CartItemRequest request) {
        return modelMapper.map(request, Cart.class);
    }

    public void toExistsEntity(CartItemRequest request, Cart cart) {
        modelMapper.map(request, cart);
    }

    public CartItemResponse toResponse(Cart cart) {
        CartItemResponse response = modelMapper.map(cart, CartItemResponse.class);
        response.setVariant(variantConverter.toResponse(cart.getProductVariantValue()));
        response.setProduct(productConverter.toResponse(cart.getProductVariantValue().getProduct()));
        return response;
    }
}

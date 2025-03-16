package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.CartConverter;
import com.agri_supplies_shop.dto.request.CartItemRequest;
import com.agri_supplies_shop.dto.response.CartItemResponse;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.entity.CartItem;
import com.agri_supplies_shop.entity.ProductVariantValue;
import com.agri_supplies_shop.entity.Users;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.CartRepository;
import com.agri_supplies_shop.repository.ProductVariantValueRepository;
import com.agri_supplies_shop.repository.UserRepository;
import com.agri_supplies_shop.service.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements CartService {
    ProductVariantValueRepository productVariantValueRepository;

    UserRepository userRepository;

    CartRepository cartRepository;

    CartConverter cartConverter;

    @Override
    public CartItemResponse addToCart(CartItemRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Users user = userRepository.findByUserName(userName).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );

        CartItem cartItem = cartRepository.findByProductVariantValueIdAndUserId(
                request.getProductVariantId(), user.getId()
        );
        if (cartItem != null) {
            cartConverter.toExistsEntity(request, cartItem);
        } else {
            cartItem = cartConverter.toEntity(request);
            cartItem.setUser(user);
        }
        ProductVariantValue productVariant = productVariantValueRepository.findById(request.getProductVariantId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_VALUE_NOT_FOUND));
        cartItem.setProductVariantValue(productVariant);
        return cartConverter.toResponse(cartRepository.save(cartItem));
    }

    @Override
    public PageResponse<CartItemResponse> getAll(int page, int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Users user = userRepository.findByUserName(userName).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        Pageable pageable = PageRequest.of(page - 1, size,
                Sort.by(Sort.Direction.DESC, "id"));
        Page<CartItem> pageData = cartRepository.findAllByUserId(user.getId(), pageable);
        return PageResponse.<CartItemResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .totalPage(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .data(pageData.getContent().stream().map(
                        it -> cartConverter.toResponse(it)
                ).toList())
                .build();
    }

    @Override
    public void delete(Long cartItemId) {
        cartRepository.deleteById(cartItemId);
    }
}

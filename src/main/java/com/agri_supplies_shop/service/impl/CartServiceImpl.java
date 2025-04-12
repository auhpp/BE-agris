package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.CartConverter;
import com.agri_supplies_shop.dto.request.CartItemRequest;
import com.agri_supplies_shop.dto.response.CartItemResponse;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.entity.Account;
import com.agri_supplies_shop.entity.Cart;
import com.agri_supplies_shop.entity.Customer;
import com.agri_supplies_shop.entity.ProductVariantValue;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.AccountRepository;
import com.agri_supplies_shop.repository.CartRepository;
import com.agri_supplies_shop.repository.CustomerRepository;
import com.agri_supplies_shop.repository.ProductVariantValueRepository;
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

    CustomerRepository customerRepository;

    AccountRepository accountRepository;

    CartRepository cartRepository;

    CartConverter cartConverter;

    @Override
    public CartItemResponse addToCart(CartItemRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Account account = accountRepository.findByUserName(userName).orElseThrow(
                () -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED)
        );
        Customer customer = customerRepository.findByAccountId(account.getId()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );

        Cart cart = cartRepository.findByProductVariantValueIdAndCustomerId(
                request.getProductVariantId(), customer.getId()
        );
        if (cart != null) {
            cartConverter.toExistsEntity(request, cart);
        } else {
            cart = cartConverter.toEntity(request);
            cart.setCustomer(customer);
        }
        ProductVariantValue productVariant = productVariantValueRepository.findById(request.getProductVariantId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_VALUE_NOT_FOUND));
        cart.setProductVariantValue(productVariant);
        return cartConverter.toResponse(cartRepository.save(cart));
    }

    @Override
    public PageResponse<CartItemResponse> getAll(int page, int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Account account = accountRepository.findByUserName(userName).orElseThrow(
                () -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED)
        );
        Customer customer = customerRepository.findByAccountId(account.getId()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        Pageable pageable = PageRequest.of(page - 1, size,
                Sort.by(Sort.Direction.DESC, "id"));
        Page<Cart> pageData = cartRepository.findAllByCustomerId(customer.getId(), pageable);
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

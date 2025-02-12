package com.agri_supplies_shop.service.impl;


import com.agri_supplies_shop.converter.ProductVariantValueConverter;
import com.agri_supplies_shop.dto.request.VariantValueRequest;
import com.agri_supplies_shop.dto.response.ProductVariantValueResponse;
import com.agri_supplies_shop.entity.ProductVariantValue;
import com.agri_supplies_shop.entity.VariantValue;
import com.agri_supplies_shop.enums.Status;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.ProductRepository;
import com.agri_supplies_shop.repository.ProductVariantValueRepository;
import com.agri_supplies_shop.repository.VariantValueRepository;
import com.agri_supplies_shop.service.ProductVariantValueService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductVariantValueImpl implements ProductVariantValueService {
    VariantValueRepository variantValueRepository;

    ProductVariantValueRepository productVariantValueRepository;

    ProductVariantValueConverter variantValueConverter;

    ProductRepository productRepository;

    @Override
    @Transactional
    public ProductVariantValueResponse create(VariantValueRequest request, Long productId) {
        //Product variant value
        ProductVariantValue productVariantValue;
        if (request.getId() != null) {
            productVariantValue = productVariantValueRepository.findById(request.getId()).orElseThrow(
                    () -> new AppException(ErrorCode.PRODUCT_VARIANT_VALUE_NOT_FOUND)
            );
            variantValueConverter.toExistsEntity(request, productVariantValue);
        } else {
            productVariantValue = variantValueConverter.toEntity(request);
        }
        //Create sku
        List<String> variantIds = request.getVariantCombination().stream().map(
                it -> {
                    VariantValue variantValue = variantValueRepository.findByValue(it);
                    return variantValue.getId().toString();
                }
        ).toList();
        String sku = String.join("-", variantIds);
        productVariantValue.setSku(sku);
        productVariantValue.setProduct(productRepository.findById(productId).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)
        ));
        productVariantValue.setStatus(Status.ACTIVE);
        //Save product variant value
        return variantValueConverter.toResponse(productVariantValueRepository.save(productVariantValue));
    }

    @Override
    @Transactional
    public Boolean delete(List<Long> ids) {
        ids.forEach(
                it -> {
                    ProductVariantValue productVariant = productVariantValueRepository.findById(it).orElseThrow(
                            () -> new AppException(ErrorCode.PRODUCT_VARIANT_VALUE_NOT_FOUND)
                    );
                    if (productVariant.getOrderItems().isEmpty()) {
                        productVariantValueRepository.deleteById(it);
                    } else {
                        throw new AppException(ErrorCode.DELETE_FAILED);
                    }
                }
        );
        return true;
    }
}

package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.dto.request.VariantRequest;
import com.agri_supplies_shop.entity.Variant;
import com.agri_supplies_shop.entity.VariantValue;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.VariantRepository;
import com.agri_supplies_shop.repository.VariantValueRepository;
import com.agri_supplies_shop.service.VariantValueService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VariantValueServiceImpl implements VariantValueService {
    VariantValueRepository variantValueRepository;

    VariantRepository variantRepository;

    @Override
    @Transactional
    public void create(VariantRequest request) {
        //Variant
        Variant variant = variantRepository.findByName(request.getName());
        if (variant == null) {
            variant = Variant.builder().name(request.getName()).build();
            variantRepository.save(variant);
        }

        //Variant value
        Variant finalVariant = variant;
        List<VariantValue> variantValues = request.getValues().stream().map(
                it -> {
                    VariantValue variantValue = variantValueRepository.findByValue(it);
                    if (variantValue == null)
                        return VariantValue.builder().value(it).variant(finalVariant).build();
                    else
                        return variantValue;
                }
        ).toList();
        variantValueRepository.saveAll(variantValues);
    }
}

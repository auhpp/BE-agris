package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.dto.request.VariantRequest;
import com.agri_supplies_shop.entity.Variant;
import com.agri_supplies_shop.entity.VariantValue;
import com.agri_supplies_shop.repository.VariantRepository;
import com.agri_supplies_shop.repository.VariantValueRepository;
import com.agri_supplies_shop.service.VariantValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VariantValueServiceImpl implements VariantValueService {
    @Autowired
    private VariantValueRepository variantValueRepository;

    @Autowired
    private VariantRepository variantRepository;

    @Override
    @Transactional
    public void createVariantValue(VariantRequest variantRequest) {
        //Variant
        Variant variant = variantRepository.findByName(variantRequest.getName());
        if (variant == null) {
            variant = Variant.builder().name(variantRequest.getName()).build();
            variantRepository.save(variant);
        }

        //Variant value
        Variant finalVariant = variant;
        List<VariantValue> variantValues = variantRequest.getValues().stream().map(
                it -> {
                    VariantValue variantValue = variantValueRepository.findByValue(it);
                    if(variantValue == null)
                        return VariantValue.builder().value(it).variant(finalVariant).build();
                    else
                        return variantValue;
                }
        ).toList();
        variantValueRepository.saveAll(variantValues);
    }
}

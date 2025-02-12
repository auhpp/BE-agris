package com.agri_supplies_shop.converter;

import com.agri_supplies_shop.dto.request.SupplierRequest;
import com.agri_supplies_shop.dto.response.SupplierResponse;
import com.agri_supplies_shop.entity.Supplier;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SupplierConverter {
    ModelMapper modelMapper;

    public SupplierResponse toResponse(Supplier supplier){
        return modelMapper.map(supplier, SupplierResponse.class);
    }

    public Supplier toEntity(SupplierRequest supplier){
        return modelMapper.map(supplier, Supplier.class);
    }
}

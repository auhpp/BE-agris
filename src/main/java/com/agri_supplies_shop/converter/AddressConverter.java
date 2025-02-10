package com.agri_supplies_shop.converter;

import com.agri_supplies_shop.dto.request.AddressRequest;
import com.agri_supplies_shop.dto.response.AddressResponse;
import com.agri_supplies_shop.entity.Address;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddressConverter {
    @Autowired
    private ModelMapper modelMapper;

    public Address toEntity(AddressRequest request){
        return modelMapper.map(request, Address.class);
    }

    public AddressResponse toResponse(Address address){
        return modelMapper.map(address, AddressResponse.class);
    }

    public void toExistsEntity(Address address, AddressRequest request){
        modelMapper.map(request, address);
    }
}

package com.agri_supplies_shop.converter;

import com.agri_supplies_shop.dto.request.UserRequest;
import com.agri_supplies_shop.dto.response.AddressResponse;
import com.agri_supplies_shop.dto.response.UserResponse;
import com.agri_supplies_shop.entity.Users;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserConverter {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AddressConverter addressConverter;

    public Users toEntity(UserRequest request) {
        return modelMapper.map(request, Users.class);
    }

    public void toExistsEntity(UserRequest request, Users user) {
        modelMapper.map(request, user);
    }

    public UserResponse toResponse(Users user) {
        UserResponse response = modelMapper.map(user, UserResponse.class);
        response.setRole(user.getRole().getName());
        if (user.getAddresses() != null) {
            List<AddressResponse> addressResponses = user.getAddresses().stream().map(
                    it -> addressConverter.toResponse(it)
            ).toList();
            response.setAddresses(addressResponses);
        }
        return response;
    }
}

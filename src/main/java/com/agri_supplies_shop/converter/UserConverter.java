package com.agri_supplies_shop.converter;

import com.agri_supplies_shop.dto.request.UserRequest;
import com.agri_supplies_shop.dto.response.AddressResponse;
import com.agri_supplies_shop.dto.response.UserResponse;
import com.agri_supplies_shop.entity.Users;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserConverter {
    ModelMapper modelMapper;

    AddressConverter addressConverter;

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

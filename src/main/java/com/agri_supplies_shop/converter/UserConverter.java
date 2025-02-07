package com.agri_supplies_shop.converter;

import com.agri_supplies_shop.dto.request.UserRequest;
import com.agri_supplies_shop.dto.response.UserResponse;
import com.agri_supplies_shop.entity.Users;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    @Autowired
    private ModelMapper modelMapper;

    public Users toUserEntity(UserRequest request){
        return modelMapper.map(request, Users.class);
    }

    public UserResponse toUserResponse(Users user){
        return modelMapper.map(user, UserResponse.class);
    }
}

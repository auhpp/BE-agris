package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.UserRequest;
import com.agri_supplies_shop.dto.response.UserResponse;

public interface UserService {
    UserResponse createUser(UserRequest request);
    UserResponse getMyInfo();
}

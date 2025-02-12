package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.AddressRequest;
import com.agri_supplies_shop.dto.request.AuthenticationRequest;
import com.agri_supplies_shop.dto.request.UserRequest;
import com.agri_supplies_shop.dto.response.AddressResponse;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest request);
    UserResponse updateUser(Long id, UserRequest request);
    UserResponse getMyInfo();
    UserResponse updateRole(Long userId, Long roleId);
    PageResponse<UserResponse> getAllUser(int page, int size);
    void deleteUser(List<Long> ids);
    AddressResponse createAndUpdateAddress(AddressRequest request);
    void deleteAddress(Long addressId);
    UserResponse updatePassword(AuthenticationRequest request);
}

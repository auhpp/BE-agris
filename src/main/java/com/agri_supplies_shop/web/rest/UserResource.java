package com.agri_supplies_shop.web.rest;

import com.agri_supplies_shop.dto.request.AddressRequest;
import com.agri_supplies_shop.dto.request.AuthenticationRequest;
import com.agri_supplies_shop.dto.request.UserRequest;
import com.agri_supplies_shop.dto.response.AddressResponse;
import com.agri_supplies_shop.dto.response.ApiResponse;
import com.agri_supplies_shop.dto.response.UserResponse;
import com.agri_supplies_shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserResource {

    @Autowired
    private UserService userService;

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody UserRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .result(userService.createUser(request))
                .build();
    }

    @GetMapping("/myInfo")
    public ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .result(userService.getMyInfo())
                .build();
    }

    @PostMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .result(userService.updateUser(id, request))
                .build();
    }

    @PostMapping("/{userId}/role/{roleId}")
    public ApiResponse<UserResponse> updateRole(@PathVariable(name = "userId") Long userId,
                                                @PathVariable(name = "roleId") Long roleId) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .result(userService.updateRole(userId, roleId))
                .build();
    }

    @PostMapping("/address")
    public ApiResponse<AddressResponse> createAndUpdateAddress(@RequestBody AddressRequest request) {
        return ApiResponse.<AddressResponse>builder()
                .code(200)
                .result(userService.createAndUpdateAddress(request))
                .build();
    }

    @PostMapping("/address/{addressId}")
    public void deleteAddress(@PathVariable("addressId") Long addressId) {
        userService.deleteAddress(addressId);
    }

    @PostMapping("/password")
    public ApiResponse<UserResponse> updatePassword(@RequestBody AuthenticationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .result(userService.updatePassword(request))
                .build();
    }
}

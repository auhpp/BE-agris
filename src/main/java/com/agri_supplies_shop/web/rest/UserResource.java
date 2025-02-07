package com.agri_supplies_shop.web.rest;

import com.agri_supplies_shop.dto.request.UserRequest;
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
    public ApiResponse<UserResponse> getMyInfo(){
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .result(userService.getMyInfo())
                .build();
    }
}

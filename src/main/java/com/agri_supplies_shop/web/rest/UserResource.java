package com.agri_supplies_shop.web.rest;

import com.agri_supplies_shop.dto.request.AddressRequest;
import com.agri_supplies_shop.dto.request.AuthenticationRequest;
import com.agri_supplies_shop.dto.request.UserRequest;
import com.agri_supplies_shop.dto.response.AddressResponse;
import com.agri_supplies_shop.dto.response.ApiResponse;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.dto.response.UserResponse;
import com.agri_supplies_shop.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserResource {

    UserService userService;

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

    @GetMapping
    public ApiResponse<PageResponse<UserResponse>> getAllUser(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                              @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<UserResponse>>builder()
                .code(200)
                .result(userService.getAllUser(page, size))
                .build();
    }

    @PostMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .result(userService.updateUser(id, request))
                .build();
    }

    @DeleteMapping("/{ids}")
    public void deleteUser(@PathVariable(name = "ids") List<Long> ids) {
        userService.deleteUser(ids);
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

    @DeleteMapping("/address/{addressId}")
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

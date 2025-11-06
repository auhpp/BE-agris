package com.agri_supplies_shop.web.rest;


import com.agri_supplies_shop.dto.request.AuthenticationRequest;
import com.agri_supplies_shop.dto.request.PasswordRequest;
import com.agri_supplies_shop.dto.response.AccountResponse;
import com.agri_supplies_shop.dto.response.ApiResponse;
import com.agri_supplies_shop.service.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountResource {
    AccountService accountService;

    @PostMapping("/password/forget")
    public ApiResponse<AccountResponse> forgetPassword(@RequestBody PasswordRequest request) {
        return ApiResponse.<AccountResponse>builder()
                .code(200)
                .result(accountService.forgetPassword(request))
                .build();
    }

    @PostMapping("/password/change")
    public ApiResponse<AccountResponse> changePassword(@RequestBody PasswordRequest request) {
        return ApiResponse.<AccountResponse>builder()
                .code(200)
                .result(accountService.changePassword(request))
                .build();
    }

    @PostMapping("/password/validate")
    public ApiResponse<Boolean> validatePassword(@RequestBody AuthenticationRequest request) {
        return ApiResponse.<Boolean>builder()
                .code(200)
                .result(accountService.validatePassword(request))
                .build();
    }

    @GetMapping
    public ApiResponse<AccountResponse> getAccount() {
        return ApiResponse.<AccountResponse>builder()
                .code(200)
                .result(accountService.getAccount())
                .build();
    }
}

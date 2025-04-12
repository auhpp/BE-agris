package com.agri_supplies_shop.web.rest;


import com.agri_supplies_shop.dto.request.PasswordRequest;
import com.agri_supplies_shop.dto.response.AccountResponse;
import com.agri_supplies_shop.dto.response.ApiResponse;
import com.agri_supplies_shop.service.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountResource {
    AccountService accountService;

    @PostMapping("/password")
    public ApiResponse<AccountResponse> updatePassword(@RequestBody PasswordRequest request) {
        return ApiResponse.<AccountResponse>builder()
                .code(200)
                .result(accountService.updatePassword(request))
                .build();
    }

}

package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.AuthenticationRequest;
import com.agri_supplies_shop.dto.request.PasswordRequest;
import com.agri_supplies_shop.dto.response.AccountResponse;

public interface AccountService {
    AccountResponse forgetPassword(PasswordRequest request);

    AccountResponse changePassword(PasswordRequest request);

    Boolean validatePassword(AuthenticationRequest request);

    AccountResponse getAccount();
}

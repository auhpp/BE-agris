package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.PasswordRequest;
import com.agri_supplies_shop.dto.response.AccountResponse;
import com.agri_supplies_shop.dto.response.CustomerResponse;

public interface AccountService {
    AccountResponse updatePassword(PasswordRequest request);
}

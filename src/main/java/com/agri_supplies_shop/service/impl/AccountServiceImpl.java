package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.dto.request.PasswordRequest;
import com.agri_supplies_shop.dto.response.AccountResponse;
import com.agri_supplies_shop.entity.Account;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.AccountRepository;
import com.agri_supplies_shop.service.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountServiceImpl implements AccountService {
    AccountRepository accountRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public AccountResponse updatePassword(PasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Account account = accountRepository.findByUserName(userName).orElseThrow(
                () -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED)
        );
        boolean valid = passwordEncoder.matches(request.getOldPassword(), account.getPassword());
        if (!valid) {
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }
        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        accountRepository.save(account);
        return AccountResponse.builder()
                .userName(account.getUserName())
                .password(account.getPassword())
                .build();
    }
}

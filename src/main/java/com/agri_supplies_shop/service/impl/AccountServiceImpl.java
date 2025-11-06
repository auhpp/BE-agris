package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.dto.request.AuthenticationRequest;
import com.agri_supplies_shop.dto.request.PasswordRequest;
import com.agri_supplies_shop.dto.response.AccountResponse;
import com.agri_supplies_shop.entity.Account;
import com.agri_supplies_shop.entity.Customer;
import com.agri_supplies_shop.entity.Staff;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.AccountRepository;
import com.agri_supplies_shop.repository.CustomerRepository;
import com.agri_supplies_shop.repository.StaffRepository;
import com.agri_supplies_shop.service.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountServiceImpl implements AccountService {
    AccountRepository accountRepository;
    PasswordEncoder passwordEncoder;
    StaffRepository staffRepository;
    CustomerRepository customerRepository;

    @Override

    public AccountResponse forgetPassword(PasswordRequest request) {
        Account account = null;
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            Staff staff = staffRepository.findByEmail(request.getEmail());
            if (staff == null) {
                Customer customer = customerRepository.findByEmail(request.getEmail()).orElseThrow(
                        () -> new AppException(ErrorCode.CUSTOMER_NOT_EXISTED)
                );
                account = customer.getAccount();
            } else
                account = staff.getAccount();

            account.setPassword(passwordEncoder.encode(request.getNewPassword()));
            accountRepository.save(account);
        }
        assert account != null;
        return AccountResponse.builder()
                .userName(account.getUserName())
                .password(account.getPassword())
                .build();
    }

    @Override
    public AccountResponse changePassword(PasswordRequest request) {
        if (Objects.equals(request.getOldPassword(), request.getNewPassword())) {
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }
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

    @Override
    public Boolean validatePassword(AuthenticationRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Account account = accountRepository.findByUserName(userName).orElseThrow(
                () -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED)
        );
        boolean valid = passwordEncoder.matches(request.getPassword(), account.getPassword());
        if (!valid) {
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }
        return true;
    }

    @Override
    public AccountResponse getAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Account account = accountRepository.findByUserName(userName).orElseThrow(
                () -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED)
        );
        return AccountResponse.builder()
                .role(account.getRole().getName())
                .password(account.getPassword())
                .userName(account.getUserName())
                .build();
    }
}

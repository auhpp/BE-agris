package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.StaffConverter;
import com.agri_supplies_shop.dto.request.StaffRequest;
import com.agri_supplies_shop.dto.request.StaffSearchRequest;
import com.agri_supplies_shop.dto.response.AccountResponse;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.dto.response.StaffResponse;
import com.agri_supplies_shop.entity.Account;
import com.agri_supplies_shop.entity.Role;
import com.agri_supplies_shop.entity.Staff;
import com.agri_supplies_shop.enums.PredefinedRole;
import com.agri_supplies_shop.enums.Status;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.AccountRepository;
import com.agri_supplies_shop.repository.RoleRepository;
import com.agri_supplies_shop.repository.StaffRepository;
import com.agri_supplies_shop.service.StaffService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StaffServiceImpl implements StaffService {

    StaffRepository staffRepository;
    StaffConverter staffConverter;
    RoleRepository roleRepository;
    AccountRepository accountRepository;

    @Override
    public AccountResponse createStaffAccount(StaffRequest request) {
        if (request.getId() != null) {
            Account account = new Account();
            Staff staff = staffRepository.findById(request.getId()).orElseThrow(
                    () -> new AppException(ErrorCode.STAFF_NOT_EXISTED)
            );
            if (request.getUserName() != null && request.getPassword() != null) {
                account = new Account();
                account.setUserName(request.getUserName());
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
                account.setPassword(passwordEncoder.encode(request.getPassword()));
                account.setCreatedAt(ZonedDateTime.now());
                Role role = roleRepository.findByName(PredefinedRole.ADMIN_ROLE.getName());
                account.setRole(role);
                accountRepository.save(account);

                staff.setStatus(Status.ACTIVE);
                staff.setAccount(account);
                staffRepository.save(staff);
                return AccountResponse.builder()
                        .userName(account.getUserName())
                        .password(account.getPassword())
                        .build();
            } else {
                throw new AppException(ErrorCode.STAFF_NOT_EXISTED);
            }
        } else {
            throw new AppException(ErrorCode.STAFF_NOT_EXISTED);
        }
    }

    @Override
    public PageResponse<StaffResponse> search(StaffSearchRequest request, int page, int size) {
        if (!Objects.equals(request.getEmail(), "")) {
            return PageResponse.<StaffResponse>builder()
                    .currentPage(page)
                    .totalElements(1)
                    .totalPage(1)
                    .pageSize(size)
                    .data(
                            List.of(staffConverter.toResponse(staffRepository.findByEmail(request.getEmail()))))
                    .build();
        } else if (!Objects.equals(request.getPhoneNumber(), "")) {
            return PageResponse.<StaffResponse>builder()
                    .currentPage(page)
                    .totalElements(1)
                    .totalPage(1)
                    .pageSize(size)
                    .data(
                            List.of(staffConverter.toResponse(staffRepository.findByPhoneNumber(request.getPhoneNumber())))
                    )
                    .build();
        } else {
            Pageable pageable = PageRequest.of(page - 1, size,
                    Sort.by(Sort.Direction.DESC, "id"));
            Page<Staff> pageData = staffRepository.findByFullNameContaining(request.getFullName(), pageable);
            return PageResponse.<StaffResponse>builder()
                    .currentPage(page)
                    .totalElements(pageData.getTotalElements())
                    .totalPage(pageData.getTotalPages())
                    .pageSize(pageData.getSize())
                    .data(pageData.getContent().stream().map(
                            it -> staffConverter.toResponse(it)
                    ).toList())
                    .build();
        }

    }
}

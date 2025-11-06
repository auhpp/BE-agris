package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.StaffConverter;
import com.agri_supplies_shop.dto.request.StaffRequest;
import com.agri_supplies_shop.dto.request.StaffSearchRequest;
import com.agri_supplies_shop.dto.response.AccountResponse;
import com.agri_supplies_shop.dto.response.ImageResponse;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.dto.response.StaffResponse;
import com.agri_supplies_shop.entity.Account;
import com.agri_supplies_shop.entity.Customer;
import com.agri_supplies_shop.entity.Role;
import com.agri_supplies_shop.entity.Staff;
import com.agri_supplies_shop.enums.PredefinedRole;
import com.agri_supplies_shop.enums.Status;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.AccountRepository;
import com.agri_supplies_shop.repository.CustomerRepository;
import com.agri_supplies_shop.repository.RoleRepository;
import com.agri_supplies_shop.repository.StaffRepository;
import com.agri_supplies_shop.repository.specification.BaseSpecification;
import com.agri_supplies_shop.repository.specification.SearchCriteria;
import com.agri_supplies_shop.service.ImageService;
import com.agri_supplies_shop.service.StaffService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StaffServiceImpl implements StaffService {

    StaffRepository staffRepository;
    StaffConverter staffConverter;
    RoleRepository roleRepository;
    AccountRepository accountRepository;
    CustomerRepository customerRepository;
    ImageService imageService;

    @Override
    public StaffResponse getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Account account = accountRepository.findByUserName(userName).orElseThrow(
                () -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED)
        );
        Staff staff = staffRepository.findByAccountId(account.getId()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        return staffConverter.toResponse(
                staff
        );
    }

    @Override
    public AccountResponse createStaffAccount(StaffRequest request) {
        if (request.getId() != null) {
            Account account = new Account();
            Staff staff = staffRepository.findById(request.getId()).orElseThrow(
                    () -> new AppException(ErrorCode.STAFF_NOT_EXISTED)
            );
            if (request.getUserName() != null && request.getPassword() != null) {
                if (staff.getAccount() != null) {
                    account = new Account();

                } else account = staff.getAccount();
                account.setStatus(Status.ACTIVE);
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
    public StaffResponse updateStaff(Long id, StaffRequest request) {
        Staff staff = staffRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.STAFF_NOT_EXISTED)
        );
        if (!Objects.equals(staff.getEmail(), request.getEmail())) {
            Optional<Customer> validate = customerRepository.findByEmail(request.getEmail());
            if (validate.isPresent()) {
                throw new AppException(ErrorCode.EMAIL_EXIT);
            }
            Staff validateStaff = staffRepository.findByEmail(request.getEmail());
            if (validateStaff != null) {
                throw new AppException(ErrorCode.EMAIL_EXIT);
            }
        }
        Account account = staff.getAccount();
        if (Objects.equals(request.getStatus(), Status.ACTIVE.name())) {
            staff.setStatus(Status.ACTIVE);
            account.setStatus(Status.ACTIVE);
        } else {
            staff.setStatus(Status.INACTIVE);
            account.setStatus(Status.INACTIVE);
        }
        accountRepository.save(account);
        staffConverter.toExistsEntity(request, staff);
        staffRepository.save(staff);
        return staffConverter.toResponse(staff);
    }

    @Override
    public PageResponse<StaffResponse> search(StaffSearchRequest request, int page, int size) {
        Specification<Staff> spec = Specification.where((root, query, criteriaBuilder) ->
                criteriaBuilder.conjunction());
        if (!Objects.equals(request.getId(), null)
        ) {
            BaseSpecification<Staff> specId = new BaseSpecification(
                    SearchCriteria.builder()
                            .key("id")
                            .value(request.getId())
                            .operation("=")
                            .build()
            );
            spec = spec.and(specId);
        }
        if (!Objects.equals(request.getFullName(), "") &&
                !Objects.equals(request.getFullName(), null)
        ) {
            BaseSpecification<Staff> specName = new BaseSpecification(
                    SearchCriteria.builder()
                            .key("fullName")
                            .value(request.getFullName())
                            .operation(":")
                            .build()
            );
            spec = spec.and(specName);
        }
        if (!Objects.equals(request.getEmail(), "") &&
                !Objects.equals(request.getEmail(), null)) {
            BaseSpecification<Staff> specEmail = new BaseSpecification(
                    SearchCriteria.builder()
                            .key("email")
                            .value(request.getEmail())
                            .operation("=")
                            .build()
            );
            spec = spec.and(specEmail);
        }
        if (!Objects.equals(request.getPhoneNumber(), "") &&
                !Objects.equals(request.getPhoneNumber(), null)
        ) {
            BaseSpecification<Staff> specPhoneNumber = new BaseSpecification(
                    SearchCriteria.builder()
                            .key("phoneNumber")
                            .value(request.getPhoneNumber())
                            .operation("=")
                            .build()
            );
            spec = spec.and(specPhoneNumber);
        }
        if (!Objects.equals(request.getStatus(), "") &&
                !Objects.equals(request.getStatus(), null)) {
            BaseSpecification<Staff> specStatus = new BaseSpecification(
                    SearchCriteria.builder()
                            .key("status")
                            .value(request.getStatus())
                            .operation("=")
                            .build()
            );
            spec = spec.and(specStatus);
        }
        Pageable pageable = PageRequest.of(page - 1, size,
                Sort.by(Sort.Direction.DESC, "id"));
        Page<Staff> pageData = staffRepository.findAll(spec, pageable);
        return PageResponse.<StaffResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .totalPage(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .data(pageData.getContent().stream().map(
                        staffConverter::toResponse
                ).toList())
                .build();

    }

    @Override
    public ImageResponse uploadAvatar(MultipartFile avatar, Long id) throws IOException {
        Staff staff = staffRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.STAFF_NOT_EXISTED)
        );
        if (staff.getAvatar() != null && !staff.getAvatar().isEmpty() && avatar != null && !avatar.isEmpty()) {
            String pathUrlImg = staff.getAvatar();
            String nameImg = pathUrlImg.substring(
                    pathUrlImg.lastIndexOf("/") + 1
            );
            imageService.deleteImgLocal(nameImg);
        }
        ImageResponse pathUrl = imageService.saveImage(avatar);
        staff.setAvatar(pathUrl.getFilePath());
        staffRepository.save(staff);
        return pathUrl;
    }

    @Override
    public Boolean recallStaff(Long staffId) {
        Staff staff = staffRepository.findById(staffId).orElseThrow(
                () -> new AppException(ErrorCode.STAFF_NOT_EXISTED)
        );
        staffRepository.delete(staff);
        accountRepository.deleteById(staff.getAccount().getId());
        return true;
    }
}

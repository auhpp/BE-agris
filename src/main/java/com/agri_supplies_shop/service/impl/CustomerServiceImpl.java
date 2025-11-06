package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.AddressConverter;
import com.agri_supplies_shop.converter.CustomerConverter;
import com.agri_supplies_shop.dto.request.AddressRequest;
import com.agri_supplies_shop.dto.request.CustomerRequest;
import com.agri_supplies_shop.dto.request.CustomerSearchRequest;
import com.agri_supplies_shop.dto.response.AddressResponse;
import com.agri_supplies_shop.dto.response.CustomerResponse;
import com.agri_supplies_shop.dto.response.ImageResponse;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.entity.*;
import com.agri_supplies_shop.enums.PredefinedRole;
import com.agri_supplies_shop.enums.Status;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.*;
import com.agri_supplies_shop.repository.specification.BaseSpecification;
import com.agri_supplies_shop.repository.specification.SearchCriteria;
import com.agri_supplies_shop.service.CustomerService;
import com.agri_supplies_shop.service.ImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerServiceImpl implements CustomerService {
    CustomerRepository customerRepository;

    RoleRepository roleRepository;

    AddressRepository addressRepository;

    CustomerConverter customerConverter;

    AddressConverter addressConverter;

    PasswordEncoder passwordEncoder;

    ImageService imageService;
    StaffRepository staffRepository;

    AccountRepository accountRepository;


    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        if (accountRepository.findByUserName(request.getUserName()).isPresent()
        ) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        if (customerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.EMAIL_EXIT);
        }
        if (staffRepository.findByEmail(request.getEmail()) != null) {
            throw new AppException(ErrorCode.EMAIL_EXIT);
        } else {
            Account account = new Account();
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            account.setUserName(request.getUserName());
            account.setPassword(passwordEncoder.encode(request.getPassword()));
            account.setCreatedAt(ZonedDateTime.now());
            Role role = roleRepository.findByName(PredefinedRole.USER_ROLE.getName());
            account.setRole(role);
            accountRepository.save(account);
            Customer customer = new Customer();
            customer.setEmail(request.getEmail());
            customer.setAccount(account);
            customer.setCreatedAt(ZonedDateTime.now());
            customer.setStatus(Status.ACTIVE);
            customerRepository.save(customer);
            return customerConverter.toResponse(customer);
        }
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        Customer user = customerRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        if (!Objects.equals(user.getEmail(), request.getEmail())) {
            Optional<Customer> validate = customerRepository.findByEmail(request.getEmail());
            if (validate.isPresent()) {
                throw new AppException(ErrorCode.EMAIL_EXIT);
            }
            Staff validateStaff = staffRepository.findByEmail(request.getEmail());
            if (validateStaff != null) {
                throw new AppException(ErrorCode.EMAIL_EXIT);
            }
        }
        Account account = user.getAccount();
        if (Objects.equals(request.getStatus(), Status.ACTIVE.name())) {
            user.setStatus(Status.ACTIVE);
            account.setStatus(Status.ACTIVE);
        } else {
            user.setStatus(Status.INACTIVE);
            account.setStatus(Status.INACTIVE);
        }
        accountRepository.save(account);
        customerConverter.toExistsEntity(request, user);
        customerRepository.save(user);
        return customerConverter.toResponse(user);
    }

    @Override
    public CustomerResponse getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Account account = accountRepository.findByUserName(userName).orElseThrow(
                () -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED)
        );
        Customer customer = customerRepository.findByAccountId(account.getId()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        return customerConverter.toResponse(
                customer
        );
    }


    @Override
    public PageResponse<CustomerResponse> search(CustomerSearchRequest request, int page, int size) {
        Specification<Customer> spec = Specification.where((root, query, criteriaBuilder) ->
                criteriaBuilder.conjunction());
        if (!Objects.equals(request.getId(), null)
        ) {
            BaseSpecification<Customer> specId = new BaseSpecification(
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
            BaseSpecification<Customer> specName = new BaseSpecification(
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
            BaseSpecification<Customer> specEmail = new BaseSpecification(
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
            BaseSpecification<Customer> specPhoneNumber = new BaseSpecification(
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
            BaseSpecification<Customer> specStatus = new BaseSpecification(
                    SearchCriteria.builder()
                            .key("status")
                            .value(request.getStatus())
                            .operation("=")
                            .build()
            );
            spec = spec.and(specStatus);
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Customer> pageData = customerRepository.findAll(spec, pageable);
        return PageResponse.<CustomerResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .totalPage(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .data(pageData.getContent().stream().map(
                        customerConverter::toResponse
                ).toList())
                .build();
    }

    @Override
    @Transactional
    public void deleteUser(List<Long> ids) {
        customerRepository.deleteByIdIn(ids);
    }

    @Override
    public AddressResponse createAndUpdateAddress(AddressRequest request) {
        Address address;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Account account = accountRepository.findByUserName(userName).orElseThrow(
                () -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED)
        );
        Customer customer = customerRepository.findByAccountId(account.getId()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );

        if (request.getId() == null) {
            address = addressConverter.toEntity(request);
            address.setCustomer(customer);
            if (customer.getAddresses().isEmpty()) {
                address.setDefaultChoice(true);
            }
        } else {
            address = addressRepository.findById(request.getId()).get();
            addressConverter.toExistsEntity(address, request);
        }
        if (request.getDefaultChoice()) {
            Address addr = addressRepository.findByDefaultChoiceAndCustomerId(true, customer.getId());
            if (addr != null && !Objects.equals(addr.getId(), request.getId())) {
                addr.setDefaultChoice(false);
                addressRepository.save(addr);
            }
        }
        return addressConverter.toResponse(addressRepository.save(address));
    }

    @Override
    public void deleteAddress(Long addressId) {
        addressRepository.deleteById(addressId);
    }


    @Override
    @Transactional
    public ImageResponse uploadAvatar(MultipartFile avatar, Long id) throws IOException {
        Customer customer = customerRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        if (customer.getAvatar() != null && !customer.getAvatar().isEmpty() && avatar != null && !avatar.isEmpty()) {
            String pathUrlImg = customer.getAvatar();
            String nameImg = pathUrlImg.substring(
                    pathUrlImg.lastIndexOf("/") + 1
            );
            imageService.deleteImgLocal(nameImg);
        }
        ImageResponse pathUrl = imageService.saveImage(avatar);
        customer.setAvatar(pathUrl.getFilePath());
        customerRepository.save(customer);
        return pathUrl;
    }


}

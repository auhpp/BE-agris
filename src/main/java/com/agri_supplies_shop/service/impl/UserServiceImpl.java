package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.AddressConverter;
import com.agri_supplies_shop.converter.UserConverter;
import com.agri_supplies_shop.dto.request.AddressRequest;
import com.agri_supplies_shop.dto.request.AuthenticationRequest;
import com.agri_supplies_shop.dto.request.LogoutRequest;
import com.agri_supplies_shop.dto.request.UserRequest;
import com.agri_supplies_shop.dto.response.AddressResponse;
import com.agri_supplies_shop.dto.response.UserResponse;
import com.agri_supplies_shop.entity.Address;
import com.agri_supplies_shop.entity.Role;
import com.agri_supplies_shop.entity.Users;
import com.agri_supplies_shop.enums.PredefinedRole;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.AddressRepository;
import com.agri_supplies_shop.repository.RoleRepository;
import com.agri_supplies_shop.repository.UserRepository;
import com.agri_supplies_shop.service.AuthenticationService;
import com.agri_supplies_shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private AddressConverter addressConverter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public UserResponse createUser(UserRequest request) {
        Users user;
        if (userRepository.findByUserName(request.getUserName()).isPresent()) {
            throw new AppException(ErrorCode.USER_EXISTED);
        } else {
            user = userConverter.toEntity(request);
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setCreatedAt(ZonedDateTime.now());
            Role role = roleRepository.findByName(PredefinedRole.USER_ROLE.getName());
            user.setRole(role);
            userRepository.save(user);
        }
        return userConverter.toResponse(user);
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest request) {
        Users user = userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        userConverter.toExistsEntity(request, user);
        return userConverter.toResponse(user);
    }

    @Override
    public UserResponse getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return userConverter.toResponse(
                userRepository.findByUserName(userName).orElseThrow(
                        () -> new AppException(ErrorCode.USER_NOT_EXISTED)
                )
        );
    }

    @Override
    public UserResponse updateRole(Long userId, Long roleId) {
        Users user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        user.setRole(roleRepository.findById(roleId).get());
        return userConverter.toResponse(
                userRepository.save(user)
        );
    }

    @Override
    public AddressResponse createAndUpdateAddress(AddressRequest request) {
        Address address;
        if (request.getId() == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            Users user = userRepository.findByUserName(userName).orElseThrow(
                    () -> new AppException(ErrorCode.USER_NOT_EXISTED)
            );
            address = addressConverter.toEntity(request);
            address.setUser(user);
        } else {
            address = addressRepository.findById(request.getId()).get();
            addressConverter.toExistsEntity(address, request);
        }
        return addressConverter.toResponse(addressRepository.save(address));
    }

    @Override
    public void deleteAddress(Long addressId) {
        addressRepository.deleteById(addressId);
    }

    @Override
    public UserResponse updatePassword(AuthenticationRequest request) {
        Users user = userRepository.findByUserName(request.getUserName()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userConverter.toResponse(userRepository.save(user));
    }


}

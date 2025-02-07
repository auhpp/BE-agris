package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.converter.UserConverter;
import com.agri_supplies_shop.dto.request.UserRequest;
import com.agri_supplies_shop.dto.response.UserResponse;
import com.agri_supplies_shop.entity.Role;
import com.agri_supplies_shop.entity.Users;
import com.agri_supplies_shop.enums.PredefinedRole;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.RoleRepository;
import com.agri_supplies_shop.repository.UserRepository;
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
    private UserConverter userConverter;

    @Override
    public UserResponse createUser(UserRequest request)  {
        Users user;
        if (userRepository.findByUserName(request.getUserName()).isPresent()) {
            throw new AppException(ErrorCode.USER_EXISTED);
        } else {
            user = userConverter.toUserEntity(request);
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setCreatedAt(ZonedDateTime.now());
            Role role = roleRepository.findByName(PredefinedRole.USER_ROLE.getName());
            user.setRole(role);
            userRepository.save(user);
        }
        return userConverter.toUserResponse(user);
    }

    @Override
    public UserResponse getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return userConverter.toUserResponse(
                userRepository.findByUserName(userName).orElseThrow(
                        () -> new AppException(ErrorCode.USER_NOT_EXISTED)
                )
        );
    }


}

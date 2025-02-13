package com.agri_supplies_shop.service.impl;

import com.agri_supplies_shop.dto.response.RoleResponse;
import com.agri_supplies_shop.repository.RoleRepository;
import com.agri_supplies_shop.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;

    @Override
    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream().map(
                it -> RoleResponse.builder().name(it.getName()).id(it.getId()).build()
        ).toList();
    }
}

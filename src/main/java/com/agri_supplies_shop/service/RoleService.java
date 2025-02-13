package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    List<RoleResponse> getAll();
}

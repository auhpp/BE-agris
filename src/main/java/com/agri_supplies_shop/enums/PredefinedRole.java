package com.agri_supplies_shop.enums;

import lombok.Getter;

@Getter
public enum PredefinedRole {
    USER_ROLE("USER"),
    ADMIN_ROLE("ADMIN");
    private final String name;

    PredefinedRole(String name) {
        this.name = name;
    }

}

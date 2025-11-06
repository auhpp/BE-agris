package com.agri_supplies_shop.enums;

public enum ImportGoodsStatus {
    IMPORTED_GOODS("Đã nhập hàng"), WAITING_FOR_IMPORT("Chờ nhập hàng");
    private final String name;

    ImportGoodsStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

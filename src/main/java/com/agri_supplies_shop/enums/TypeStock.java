package com.agri_supplies_shop.enums;

public enum TypeStock {
    BILL("Bán hàng"), UPDATE("Cập nhật sản phẩm"), CREATE("Tạo mới sản phẩm"),
    RECEIPT("Nhập hàng")
    ;
    private final String name;

    TypeStock(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

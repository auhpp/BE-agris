package com.agri_supplies_shop.enums;

public enum PayeeTypeEnum {
    SUPPLIER("Nhà cung cấp"), CUSTOMER("Khách hàng"), OTHER("Khác");
    private final String name;

    PayeeTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}

package com.agri_supplies_shop.enums;

public enum PaymentMethod {
    TRANSFER("Chuyển khoản"), CASH("Tiền mặt");
    private final String name;

    PaymentMethod(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}

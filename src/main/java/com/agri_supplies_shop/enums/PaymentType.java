package com.agri_supplies_shop.enums;

public enum PaymentType {
    PAY("Thanh toán");
    private final String name;

    PaymentType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}

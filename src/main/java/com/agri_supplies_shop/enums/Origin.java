package com.agri_supplies_shop.enums;

public enum Origin {
    VIET_NAM("Việt Nam"), JAPAN("Nhật Bản"), GERMANY("Đức"), INDIA("Ấn Độ");
    private final String name;

    Origin(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

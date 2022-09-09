package com.sa.product.easyExcel.test;

public enum SexEnum {
    MAN("男"),
    WOMAN("女");
    private final String name;

    SexEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

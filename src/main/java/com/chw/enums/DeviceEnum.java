package com.chw.enums;

public enum DeviceEnum {
    PHONE("phone", "手机"),
    COMPUTER("computer", "电脑");

    private String code;
    private String name;

    DeviceEnum(final String code, final String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}

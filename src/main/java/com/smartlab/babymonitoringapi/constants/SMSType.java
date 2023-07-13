package com.smartlab.babymonitoringapi.constants;

public enum SMSType {
    TRANSACTIONAL("Transactional"),
    PROMOTIONAL("Promotional");

    private final String value;

    SMSType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

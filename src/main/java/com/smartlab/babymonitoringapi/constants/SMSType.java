package com.smartlab.babymonitoringapi.constants;

import lombok.Getter;

@Getter
public enum SMSType {
    TRANSACTIONAL("Transactional"),
    PROMOTIONAL("Promotional");

    private final String value;

    SMSType(String value) {
        this.value = value;
    }
}

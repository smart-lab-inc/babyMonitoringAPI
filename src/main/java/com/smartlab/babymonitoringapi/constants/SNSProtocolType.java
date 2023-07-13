package com.smartlab.babymonitoringapi.constants;

import lombok.Getter;

@Getter
public enum SNSProtocolType {
    SMS("sms"),
    EMAIL("email");

    private final String value;

    SNSProtocolType(String value) {
        this.value = value;
    }
}

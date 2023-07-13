package com.smartlab.babymonitoringapi.services;

import com.smartlab.babymonitoringapi.constants.SMSType;

public interface ISNSService {
    void subscribeSMS(String phoneNumber);
    void sendSMS(String message, String phoneNumber, SMSType smsType);
}

package com.smartlab.babymonitoringapi.services.impls;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.smartlab.babymonitoringapi.constants.SMSType;
import com.smartlab.babymonitoringapi.constants.SNSProtocolType;
import com.smartlab.babymonitoringapi.services.ISNSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SNSServiceImpl implements ISNSService {

    @Autowired
    private AmazonSNSClient amazonSNSClient;

    @Value("${aws.sns.topicArn}")
    private String topicARN;

    @Override
    public void subscribeSMS(String phoneNumber) {
        SubscribeRequest request = new SubscribeRequest(topicARN, SNSProtocolType.SMS.getValue(), phoneNumber);
        amazonSNSClient.subscribe(request);
    }

    @Override
    public void sendSMS(String message, String phoneNumber, SMSType smsType) {
        Map<String, MessageAttributeValue> smsAttributes = new HashMap<>();

        smsAttributes.put("AWS.SNS.SMS.MaxPrice", new MessageAttributeValue()
                .withStringValue("0.50")
                .withDataType("Number"));
        smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue()
                .withStringValue(smsType.getValue())
                .withDataType("String"));

        PublishRequest publishRequest = new PublishRequest()
                .withMessage(message)
                .withPhoneNumber(phoneNumber)
                .withMessageAttributes(smsAttributes);

        amazonSNSClient.publish(publishRequest);
    }
}

package com.smartlab.babymonitoringapi.rabbit.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
public class NewSensorDataBodyRequest {

    private String name;

    private Float value;

    private String measurement;

    private String timestamp;
}

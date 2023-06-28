package com.smartlab.babymonitoringapi.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateSensorData {

    String name;

    Float value;

    String measurement;
}

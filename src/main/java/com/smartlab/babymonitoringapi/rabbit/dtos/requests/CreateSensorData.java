package com.smartlab.babymonitoringapi.rabbit.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateSensorData {

    String name;

    Float value;

    String measurement;
}

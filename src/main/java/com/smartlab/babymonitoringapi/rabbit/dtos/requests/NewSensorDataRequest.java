package com.smartlab.babymonitoringapi.rabbit.dtos.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class NewSensorDataRequest {

    private String monitorId;

    private List<NewSensorDataBodyRequest> body;
}

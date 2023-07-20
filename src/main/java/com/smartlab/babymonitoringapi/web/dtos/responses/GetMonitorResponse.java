package com.smartlab.babymonitoringapi.web.dtos.responses;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class GetMonitorResponse {
    String id;
    String serialNumber;
    String userId;
}

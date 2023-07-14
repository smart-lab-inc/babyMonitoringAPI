package com.smartlab.babymonitoringapi.services;

import com.smartlab.babymonitoringapi.rabbit.dtos.requests.NewSensorDataBodyRequest;
import com.smartlab.babymonitoringapi.web.dtos.responses.BaseResponse;

import java.util.List;

public interface ISensorDataService {

    BaseResponse createManyWithSameMonitorId(List<NewSensorDataBodyRequest> createSensorDatumRequests, String monitorId);

    BaseResponse get(String monitorId, String sensorName, String startTimestamp, String endTimestamp);

}

package com.smartlab.babymonitoringapi.web.dtos.responses;

import com.smartlab.babymonitoringapi.persistance.mongo.documents.SensorData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class GetSensorDataResponse {

    private List<SensorData> temperature;
    private List<SensorData>  movement;
    private List<SensorData>  sound;
    private List<SensorData>  humidity;

}

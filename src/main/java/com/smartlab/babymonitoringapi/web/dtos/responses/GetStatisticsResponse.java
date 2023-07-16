package com.smartlab.babymonitoringapi.web.dtos.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter @Setter
public class GetStatisticsResponse {
    private Map<String, Float> temperature;
    private Map<String, Float> movement;
    private Map<String, Float> sound;
    private Map<String, Float> humidity;
}
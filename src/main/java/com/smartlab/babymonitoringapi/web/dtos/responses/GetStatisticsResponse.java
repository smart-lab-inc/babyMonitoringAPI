package com.smartlab.babymonitoringapi.web.dtos.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter @Setter
public class GetStatisticsResponse {
    private Map<String, List<Float>> temperature;
    private Map<String, List<Float>> movement;
    private Map<String, List<Float>> sound;
    private Map<String, List<Float>> humidity;
}
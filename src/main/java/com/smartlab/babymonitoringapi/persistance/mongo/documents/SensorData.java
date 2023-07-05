package com.smartlab.babymonitoringapi.persistance.mongo.documents;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("sensorData")
@Getter @Setter @Builder
public class SensorData {

    @Id
    private String id;

    private String name;

    private Float value;

    private String measurement;
}

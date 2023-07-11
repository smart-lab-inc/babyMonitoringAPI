package com.smartlab.babymonitoringapi.persistance.mongo.documents;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("sensor_data")
@Getter @Setter @Builder
public class SensorData {

    @Id
    private String id;

    private String name;

    private Float value;

    private String measurement;

    private String monitorId;

    private LocalDateTime timestamp;
}

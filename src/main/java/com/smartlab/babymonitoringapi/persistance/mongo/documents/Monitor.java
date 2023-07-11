package com.smartlab.babymonitoringapi.persistance.mongo.documents;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("monitors")
@Getter @Setter @Builder
public class Monitor {

    @Id
    private String id;

    @Indexed(unique = true)
    private String serialNumber;

    private Boolean isActivated;

    private String userId;

    private List<String> sensorDataIds;
}

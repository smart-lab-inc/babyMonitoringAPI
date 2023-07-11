package com.smartlab.babymonitoringapi.persistance.mongo.documents;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("monitors")
@Getter
@Setter
public class Monitor {

    @Id
    private String id;

    @Indexed(unique = true)
    private String serialNumber;

    @Value("#{false}")
    private Boolean isActivated;

    @DBRef
    private User user;
}

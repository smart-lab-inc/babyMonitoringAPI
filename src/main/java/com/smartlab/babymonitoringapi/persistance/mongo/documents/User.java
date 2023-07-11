package com.smartlab.babymonitoringapi.persistance.mongo.documents;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document("users")
@Getter @Setter @Builder
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    @DBRef
    private List<Monitor> monitors;

    @Value("${my.list.of.strings:}#{T(java.util.Collections).emptyList()}")
    private List<String> monitorIds;

}

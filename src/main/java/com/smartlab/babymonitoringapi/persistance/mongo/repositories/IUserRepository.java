package com.smartlab.babymonitoringapi.persistance.mongo.repositories;

import com.smartlab.babymonitoringapi.persistance.mongo.documents.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Optional<User> findByMonitorIdsContains(String id);

}

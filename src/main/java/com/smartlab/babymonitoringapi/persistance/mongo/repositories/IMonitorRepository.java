package com.smartlab.babymonitoringapi.persistance.mongo.repositories;

import com.smartlab.babymonitoringapi.persistance.mongo.documents.Monitor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IMonitorRepository extends MongoRepository<Monitor, String> {

    @Query("{ id: '?0' }")
    Optional<Monitor> findOneById(String id);
}

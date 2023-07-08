package com.smartlab.babymonitoringapi.services;

import com.smartlab.babymonitoringapi.controllers.dtos.requests.CreateUserRequest;
import com.smartlab.babymonitoringapi.controllers.dtos.requests.UpdateUserRequest;
import com.smartlab.babymonitoringapi.controllers.dtos.responses.BaseResponse;
import com.smartlab.babymonitoringapi.persistance.mongo.documents.User;

public interface IUserService {

    BaseResponse create(CreateUserRequest request);

    BaseResponse get(String id);

    BaseResponse update(UpdateUserRequest request, String id);

    BaseResponse delete(String id);

    User findOneAndEnsureExistById(String id);
}

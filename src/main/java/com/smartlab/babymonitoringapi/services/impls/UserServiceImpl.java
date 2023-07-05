package com.smartlab.babymonitoringapi.services.impls;

import com.smartlab.babymonitoringapi.controllers.dtos.requests.CreateUserRequest;
import com.smartlab.babymonitoringapi.controllers.dtos.responses.BaseResponse;
import com.smartlab.babymonitoringapi.controllers.dtos.responses.UserResponse;
import com.smartlab.babymonitoringapi.persistance.mongo.documents.User;
import com.smartlab.babymonitoringapi.persistance.mongo.repositories.IUserRepository;
import com.smartlab.babymonitoringapi.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

//  TODO: Implementar los métodos que faltan (get, put, delete)

    @Override
    public BaseResponse create(CreateUserRequest request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }

        User user = repository.save(from(request));

        return BaseResponse.builder()
                .data(from(user))
                .message("User data saved successfully!")
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .success(true)
                .build();
    }

    User from(CreateUserRequest request) {
        return User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
    }

    UserResponse from(User user) {
        UserResponse response = new UserResponse();
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        return response;
    }
}

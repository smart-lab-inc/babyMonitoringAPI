package com.smartlab.babymonitoringapi.services.impls;

import com.smartlab.babymonitoringapi.persistance.mongo.documents.Monitor;
import com.smartlab.babymonitoringapi.persistance.mongo.documents.User;
import com.smartlab.babymonitoringapi.persistance.mongo.repositories.IUserRepository;
import com.smartlab.babymonitoringapi.services.ISNSService;
import com.smartlab.babymonitoringapi.services.IMonitorService;
import com.smartlab.babymonitoringapi.services.IUserService;
import com.smartlab.babymonitoringapi.web.controllers.exceptions.AccessDeniedException;
import com.smartlab.babymonitoringapi.web.controllers.exceptions.ObjectNotFoundException;
import com.smartlab.babymonitoringapi.web.controllers.exceptions.UniqueConstraintViolationException;
import com.smartlab.babymonitoringapi.web.dtos.requests.AddMonitorRequest;
import com.smartlab.babymonitoringapi.web.dtos.requests.CreateUserRequest;
import com.smartlab.babymonitoringapi.web.dtos.requests.UpdateUserRequest;
import com.smartlab.babymonitoringapi.web.dtos.responses.BaseResponse;
import com.smartlab.babymonitoringapi.web.dtos.responses.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository repository;

    @Autowired
    private ISNSService snsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    private IMonitorService monitorService;

    private static UserDetailsImpl getUserAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetailsImpl) authentication.getPrincipal();
    }

    @Override
    public BaseResponse create(CreateUserRequest request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }

        User user = repository.save(from(request));

        snsService.subscribeSMS(user.getPhoneNumber());

        return BaseResponse.builder()
                .data(toUserResponse(user))
                .message("User data saved successfully!")
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .success(true)
                .build();
    }

    @Override
    public BaseResponse get(String id) {
        UserDetailsImpl userDetails = getUserAuthenticated();

        if (!userDetails.getUser().getId().equals(id)) {
            throw new AccessDeniedException();
        }

        User user = findOneAndEnsureExistById(id);

        return BaseResponse.builder()
                .data(toUserResponse(user))
                .message("User found")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .success(Boolean.TRUE)
                .build();
    }

    @Override
    public Optional<User> findOneByMonitorId(String id) {
        return repository.findByMonitorIdsContains(id);
    }

    @Override
    public User update(User user) {
        return repository.save(user);
    }

    @Override
    public BaseResponse update(UpdateUserRequest request, String id) {
        User userAuthenticated = getUserAuthenticated().getUser();

        if (!userAuthenticated.getId().equals(id)) {
            throw new AccessDeniedException();
        }

        if (repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }

        userAuthenticated = update(userAuthenticated, request);
        return BaseResponse.builder()
                .data(toUserResponse(userAuthenticated))
                .message("User updated correctly")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .success(Boolean.TRUE)
                .build();
    }

    @Override
    public BaseResponse addMonitor(AddMonitorRequest request) {
        User userAuthenticated = getUserAuthenticated().getUser();

        if (userAuthenticated.getId().isEmpty()) {
            throw new AccessDeniedException();
        }

        Monitor monitor = monitorService.findOneByAndEnsureExitsBySerialNumber(request.getMonitorSerialNumber());


        if (monitor.getUserId() == null || userAuthenticated.getId().equals(monitor.getUserId())) {
            if (monitor.getId().isEmpty()) {
                throw new ObjectNotFoundException("Monitor not found");
            }

            if (userAuthenticated.getMonitorIds() == null) {
                userAuthenticated.setMonitorIds(new ArrayList<>());
            }

            if (!userAuthenticated.getMonitorIds().contains(request.getMonitorSerialNumber())) {
                userAuthenticated.getMonitorIds().add(request.getMonitorSerialNumber());
                monitor.setUserId(userAuthenticated.getId());
                monitorService.update(monitor);
                repository.save(userAuthenticated);
            } else {
                throw new UniqueConstraintViolationException("Monitor is already associated with the current user");
            }
        } else {
            throw new UniqueConstraintViolationException("Monitor is already in use");
        }

        return BaseResponse.builder()
                .data("User data saved succesfully!")
                .message("User data saved successfully!")
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .success(true)
                .build();
    }

    @Override
    public BaseResponse delete(String id) {
        User user = getUserAuthenticated().getUser();

        if (!repository.existsById(id)) {
            throw new ObjectNotFoundException("User not found");
        }

        if (!user.getId().equals(id)) {
            throw new AccessDeniedException();
        }

        repository.deleteById(id);

        return BaseResponse.builder()
                .data(Collections.EMPTY_LIST)
                .message("User deleted correctly")
                .status(HttpStatus.NO_CONTENT)
                .statusCode(HttpStatus.NO_CONTENT.value())
                .success(Boolean.TRUE)
                .build();
    }

    @Override
    public User findOneAndEnsureExistById(String id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("User not found"));
    }

    @Override
    public User findOneAndEnsureExistByMonitorId(String id) {
        return findOneByMonitorId(id).orElseThrow(() -> new ObjectNotFoundException("User not found"));
    }


    private User update(User user, UpdateUserRequest request) {
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return repository.save(user);
    }

    private User from(CreateUserRequest request) {
        return User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
    }

    private UserResponse toUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPhoneNumber(user.getPhoneNumber());
        return response;
    }
}

package com.smartlab.babymonitoringapi.services.impls;

import com.smartlab.babymonitoringapi.services.IAuthService;
import com.smartlab.babymonitoringapi.utils.JWTUtils;
import com.smartlab.babymonitoringapi.web.controllers.exceptions.AccessDeniedException;
import com.smartlab.babymonitoringapi.web.dtos.requests.AuthenticationRequest;
import com.smartlab.babymonitoringapi.web.dtos.requests.ValidateTokenRequest;
import com.smartlab.babymonitoringapi.web.dtos.responses.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final AuthenticationManager authenticationManager;

    @Value("${smartlab.app.jwtSecret}")
    private String jwtSecret;

    @Override
    public BaseResponse authenticate(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String email = userDetails.getUsername();

        String fullName = userDetails.getUser().getFirstName() + " " + userDetails.getUser().getLastName();

        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userDetails.getUser().getId());
        payload.put("fullName", fullName);

        String token = JWTUtils.generateToken(email, payload, jwtSecret, 30);

        Map<String, String> data = new HashMap<>();
        data.put("accessToken", token);

        return BaseResponse.builder()
                .data(data)
                .message("Success")
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .success(true)
                .build();
    }

    @Override
    public BaseResponse validateToken(ValidateTokenRequest request) {
        System.out.println(request.getToken());
        Boolean isTokenValid = JWTUtils.isValidateToken(request.getToken(), jwtSecret);

        if (!isTokenValid) {
            throw new AccessDeniedException();
        }

        return BaseResponse.builder()
                .message("Success")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .build();
    }
}

package com.smartlab.babymonitoringapi.services.impls;

import com.smartlab.babymonitoringapi.dtos.requests.AuthenticationRequest;
import com.smartlab.babymonitoringapi.dtos.responses.BaseResponse;
import com.smartlab.babymonitoringapi.services.IAuthService;
import com.smartlab.babymonitoringapi.utils.JWTUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service @Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;

    public BaseResponse authenticate(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String email = userDetails.getUsername();

        String fullName = userDetails.getUser().getFirstName() + " " + userDetails.getUser().getLastName();

        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userDetails.getUser().getId());
        payload.put("fullName", fullName);

        String token = jwtUtils.generateToken(email, payload);

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
}

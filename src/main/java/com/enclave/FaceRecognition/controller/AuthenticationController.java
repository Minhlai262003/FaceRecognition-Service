package com.enclave.FaceRecognition.controller;

import com.enclave.FaceRecognition.dto.Request.AuthenticationRequest;
import com.enclave.FaceRecognition.dto.Request.IntrospectRequest;
import com.enclave.FaceRecognition.dto.Request.LogoutRequest;
import com.enclave.FaceRecognition.dto.Request.RefreshRequest;
import com.enclave.FaceRecognition.dto.Response.ApiResponse;
import com.enclave.FaceRecognition.dto.Response.AuthenticationResponse;
import com.enclave.FaceRecognition.dto.Response.IntrospectResponse;
import com.enclave.FaceRecognition.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .code(1000)
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .code(1000)
                .result(result)
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .code(1000)
                .result(result)
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> authenticate(@RequestBody LogoutRequest request)
            throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
                .code(1000)
                .build();
    }
}
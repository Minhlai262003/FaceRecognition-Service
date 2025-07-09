package com.enclave.FaceRecognition.controller;

import com.enclave.FaceRecognition.dto.Request.UserCreateRequest;
import com.enclave.FaceRecognition.dto.Response.ApiResponse;
import com.enclave.FaceRecognition.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping(consumes = "multipart/form-data")
    public ApiResponse<String> createUser(@ModelAttribute @Valid UserCreateRequest request) {
        userService.createUser(request);
        return ApiResponse.<String>builder()
                .code(1000)
                .result("User created successfully")
                .build();
    }
}

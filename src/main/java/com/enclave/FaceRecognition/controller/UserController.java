package com.enclave.FaceRecognition.controller;

import com.enclave.FaceRecognition.dto.Request.UserCreateRequest;
import com.enclave.FaceRecognition.dto.Request.UserUpdateRequest;
import com.enclave.FaceRecognition.dto.Response.ApiResponse;
import com.enclave.FaceRecognition.dto.Response.UserPythonResponse;
import com.enclave.FaceRecognition.dto.Response.UserRecognitionResponse;
import com.enclave.FaceRecognition.dto.Response.UserResponse;
import com.enclave.FaceRecognition.entity.User;
import com.enclave.FaceRecognition.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@SecurityRequirement(name = "bearerAuth")
@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "User APIs")
public class UserController {
    UserService userService;

    @PostMapping(consumes = "multipart/form-data")
    public ApiResponse<String> createUser(@ModelAttribute @Valid UserCreateRequest request) {
        userService.createUser(request);
        return ApiResponse.<String>builder()
                .status(201)
                .message("User created successfully")
                .success(true)
                .build();
    }
    @PostMapping(path = "/recognize", consumes = "multipart/form-data")
    public ApiResponse<UserRecognitionResponse> recognize(@RequestParam("image") MultipartFile fileImage) {
        var response = userService.recognizeUser(fileImage);
        return ApiResponse.<UserRecognitionResponse>builder()
                .status(200)
                .message(response.getMessage())
                .success(true)
                .data(response.getUser())
                .build();
    }

    @Hidden
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteUser(@PathVariable String id) {
        userService.deleteUserById(id);
        return ApiResponse.<String>builder()
                .status(200)
                .message("Deleted success")
                .success(true)
                .build();
    }
    @Hidden
    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers(){
        List<UserResponse> users = userService.getAllUsers();
        return ApiResponse.<List<UserResponse>>builder()
                .status(200)
                .message("Success")
                .success(true)
                .data(users)
                .build();
    }
    @Hidden
    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUser(@PathVariable String id) {
        UserResponse user = userService.getUserById(id);
        return ApiResponse.<UserResponse>builder()
                .status(200)
                .message("Success")
                .success(true)
                .data(user)
                .build();
    }
    @Hidden
    @PutMapping("/{id}")
    public ApiResponse<?> updateUser(
            @PathVariable String id,
            @RequestBody @Valid UserUpdateRequest userUpdateRequest) {

        userService.updateUser(id, userUpdateRequest);

        return ApiResponse.builder()
                .status(200)
                .message("Update user successful")
                .success(true)
                .data(null).build();
    }
}

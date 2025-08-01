package com.enclave.FaceRecognition.controller;

import com.enclave.FaceRecognition.dto.Request.UserCreateRequest;
import com.enclave.FaceRecognition.dto.Response.ApiResponse;
import com.enclave.FaceRecognition.dto.Response.UserResponse;
import com.enclave.FaceRecognition.entity.User;
import com.enclave.FaceRecognition.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

//    @PostMapping(consumes = "multipart/form-data")
//    public ApiResponse<String> createUser(@ModelAttribute @Valid UserCreateRequest request) {
//        userService.createUser(request);
//        return ApiResponse.<String>builder()
//                .code(1000)
//                .result("User created successfully")
//                .build();
//    }
//
//
//    @DeleteMapping("/{id}")
//    public ApiResponse<String> deleteUser(@PathVariable String id) {
//        userService.deleteUserById(id);
//        return ApiResponse.<String>builder()
//                .code(1000)
//                .result("Deleted success")
//                .build();
//    }
//
//    @GetMapping
//    public ApiResponse<List<UserResponse>> getAllUsers(){
//        List<UserResponse> users = userService.getAllUsers();
//        return ApiResponse.<List<UserResponse>>builder()
//                .code(1000)
//                .result(users)
//                .build();
//    }


//    @PutMapping("/{id}")
//    public ResponseEntity<ApiResponse<Users>> updateUser(
//            @PathVariable Long id,
//            @RequestBody UserUpdateDTO dto) {
//
//        Users updatedUser = userService.updateUser(id, dto);
//
//        ApiResponse<Users> response = new ApiResponse<>(
//                200,
//                "Update Success",
//                updatedUser,
//                LocalDateTime.now()
//        );
//
//        return ResponseEntity.ok(response);
//    }
}

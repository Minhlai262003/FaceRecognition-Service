package com.enclave.FaceRecognition.controller;

import com.enclave.FaceRecognition.dto.Response.ApiResponse;
import com.enclave.FaceRecognition.dto.UserUpdateDTO;
import com.enclave.FaceRecognition.entity.Users;
import com.enclave.FaceRecognition.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        ApiResponse<Object> response = new ApiResponse<>(200, "Delete success", null, LocalDateTime.now());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Users>> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateDTO dto) {

        Users updatedUser = userService.updateUser(id, dto);

        ApiResponse<Users> response = new ApiResponse<>(
                200,
                "Update Success",
                updatedUser,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

}

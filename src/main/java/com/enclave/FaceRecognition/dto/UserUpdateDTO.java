package com.enclave.FaceRecognition.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String role;
    private String gender;
}

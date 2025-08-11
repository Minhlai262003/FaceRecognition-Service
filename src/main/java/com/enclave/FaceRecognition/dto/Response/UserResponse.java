package com.enclave.FaceRecognition.dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    String id;
    String email;
    String firstName;
    String lastName;
    String phoneNumber;
    LocalDate birthDay;
    String gender;
    String role;
    LocalDateTime createdAt;
}

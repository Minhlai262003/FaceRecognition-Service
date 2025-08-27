package com.enclave.FaceRecognition.dto.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({
        "id", "email", "firstName", "lastName", "phoneNumber", "birthDay",
        "gender", "role",
        "isActive", "isVerified",   // ép xuất hiện trước
        "createdAt", "updatedAt"
})
public class UserResponse {
    String id;
    String email;
    String firstName;
    String lastName;
    String phoneNumber;
    LocalDate birthDay;
    String gender;
    String role;
    @JsonProperty("isActive")
    boolean isActive;
    @JsonProperty("isVerified")
    boolean isVerified;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}

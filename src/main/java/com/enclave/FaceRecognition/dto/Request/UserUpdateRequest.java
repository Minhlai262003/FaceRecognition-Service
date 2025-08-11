package com.enclave.FaceRecognition.dto.Request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    @NotNull(message = "EMAIL_NULL")
    String email;
    @NotNull(message = "FIRST_NAME_NULL")
    String firstName;

    @NotNull(message = "LAST_NAME_NULL")
    String lastName;

    @Pattern(
            regexp = "^(0[3|5|7|8|9])[0-9]{8}$",
            message = "PHONE_INVALID"
    )
    String phoneNumber;

    LocalDate birthDay;

    String gender;
    @NotNull(message = "ROLE_NULL")
    String role;
}

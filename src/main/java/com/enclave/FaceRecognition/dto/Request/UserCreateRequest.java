package com.enclave.FaceRecognition.dto.Request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {
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

    @NotNull(message = "IMAGE_NULL")
    @NotEmpty(message = "IMAGE_NULL")
    List<MultipartFile> faceImages;
}

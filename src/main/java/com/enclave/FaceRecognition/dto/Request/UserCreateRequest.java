package com.enclave.FaceRecognition.dto.Request;


import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

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

    String gender;
    @NotNull(message = "PASSWORD_NULL")
    String password;
    String role;


    List<MultipartFile> faceImages;
}

package com.enclave.FaceRecognition.dto.Request;


import io.swagger.v3.oas.annotations.media.Schema;
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
    @NotNull(message = "Email is required")
    String email;

    @NotNull(message = "First name is required")
    String firstName;

    @NotNull(message = "Last name is required")
    String lastName;

    @Pattern(
            regexp = "^(0[3|5|7|8|9])[0-9]{8}$",
            message = "Phone number is invalid"
    )
    @NotNull(message = "Phone number is required")
    String phoneNumber;

    @NotNull(message = "Birthday is required")
    @Schema(description = "format yyyy-MM-dd", example = "2003-05-05")
    LocalDate birthDay;

    @NotNull(message = "Gender is required")
    @Schema(description = "Specifies the user's gender. Accepted values: 'male', 'female', or 'other'.")
    String gender;

    @NotNull(message = "Role is required")
    @Schema(description = "Defines the user's role in the system. Accepted values: 'admin' or 'user'.")
    String role;

    @NotNull(message = "Active field is required")
    boolean active;

    @NotNull(message = "Verified field is required")
    boolean verified;

    @NotNull(message = "Face images are required")
    @NotEmpty(message = "Face images are required")
    @Schema(
            description = "List of face images for user registration. " +
                    "Must include 10 images with different angles and expressions: " +
                    "looking straight, 90° left, 90° right, 45° left, 45° right, " +
                    "looking up, looking down, neutral face, smiling face, " +
                    "and under different lighting conditions."
    )
    List<MultipartFile> faceImages;
}

package com.enclave.FaceRecognition.dto.Request;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PythonUserCreationRequest {
    String employeeId;
    String name;
    String department;
}

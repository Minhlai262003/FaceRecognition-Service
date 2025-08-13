package com.enclave.FaceRecognition.dto.Response;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PythonResponse <T> {
    String message;
    int status;
    Boolean success;
    T user;
}

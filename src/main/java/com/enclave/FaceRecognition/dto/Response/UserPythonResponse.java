package com.enclave.FaceRecognition.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPythonResponse {
    private String id;
    private String name;
    private String department;
}

package com.enclave.FaceRecognition.mapper;


import com.enclave.FaceRecognition.dto.Response.UserPythonResponse;
import com.enclave.FaceRecognition.dto.Response.UserRecognitionResponse;

public class UserRecognitionMapper {
    public static UserRecognitionResponse fromPythonResponse(UserPythonResponse pythonResponse) {
        if (pythonResponse == null) {
            return null;
        }
        return UserRecognitionResponse.builder()
                .userID(pythonResponse.getId())
                .userName(pythonResponse.getName())
                .role(pythonResponse.getDepartment())
                .build();
    }
}

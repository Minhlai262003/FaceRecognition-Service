package com.enclave.FaceRecognition.dto.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRecognitionResponse {
    @JsonProperty("user_id")
    private String userID;
    @JsonProperty("user_name")
    private String userName;
    @JsonProperty("role")
    private String role;
}

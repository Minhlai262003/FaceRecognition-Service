package com.enclave.FaceRecognition.dto.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnknownFaceImage {
    @JsonProperty("capture_date")
    private String captureDate;
    private String image;
}

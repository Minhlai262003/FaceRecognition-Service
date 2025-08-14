package com.enclave.FaceRecognition.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordResponse {
    private Long id;
    private String word;
    private String pronunciation;
    private String partOfSpeech;
    private String mean;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

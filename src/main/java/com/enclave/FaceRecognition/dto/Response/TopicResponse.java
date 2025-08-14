package com.enclave.FaceRecognition.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

public class TopicResponse {
    private Long id;
    private String name;
    private List<WordResponse> vocabularies;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

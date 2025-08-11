package com.enclave.FaceRecognition.controller;

import com.enclave.FaceRecognition.dto.Response.ApiResponse;
import com.enclave.FaceRecognition.dto.Response.TopicResponse;
import com.enclave.FaceRecognition.entity.Topic;
import com.enclave.FaceRecognition.mapper.TopicMapper;
import com.enclave.FaceRecognition.service.TopicService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;
    private final TopicMapper topicMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TopicResponse>>> getAllTopics() {
        List<Topic> topics = topicService.fetchAllTopics();
        List<TopicResponse> response = topicMapper.toTopicResponseList(topics);
        return ResponseEntity.ok(ApiResponse.<List<TopicResponse>>builder().status(200).message("Fetched data successfully").success(true).data(response).build());
    }
}

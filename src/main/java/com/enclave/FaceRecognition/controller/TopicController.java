package com.enclave.FaceRecognition.controller;

import com.enclave.FaceRecognition.dto.Response.ApiResponse;
import com.enclave.FaceRecognition.dto.Response.TopicResponse;
import com.enclave.FaceRecognition.entity.Topic;
import com.enclave.FaceRecognition.exception.TopicNotFoundException;
import com.enclave.FaceRecognition.mapper.TopicMapper;
import com.enclave.FaceRecognition.service.TopicService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.enclave.FaceRecognition.dto.Request.UpdateTopicRequest;

import java.util.ArrayList;
import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;
    private final TopicMapper topicMapper;


    @Hidden
    @GetMapping
    public ResponseEntity<ApiResponse<List<TopicResponse>>> getAllTopics() {
        List<Topic> topics = topicService.fetchAllTopics();
        List<TopicResponse> response = topicMapper.toTopicResponseList(topics);
        return ResponseEntity.ok(ApiResponse.<List<TopicResponse>>builder().status(200).message("Fetched data successfully").success(true).data(response).build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TopicResponse>> updateTopic(
            @PathVariable Long id,
            @RequestBody UpdateTopicRequest request) {
        try {
            Topic updatedTopic = topicService.updateTopicName(id, request.getName());

            // Tránh null vocabularies
            TopicResponse response = new TopicResponse();
            response.setId(updatedTopic.getId());
            response.setName(updatedTopic.getName());
            response.setCreatedAt(updatedTopic.getCreatedAt());
            response.setUpdatedAt(updatedTopic.getUpdatedAt());

            return ResponseEntity.ok(ApiResponse.<TopicResponse>builder()
                    .status(200)
                    .message("Topic updated successfully")
                    .success(true)
                    .data(response)
                    .build());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.<TopicResponse>builder()
                    .status(400)
                    .message(e.getMessage())
                    .success(false)
                    .build());
        } catch (TopicNotFoundException e) {
            return ResponseEntity.status(404).body(ApiResponse.<TopicResponse>builder()
                    .status(404)
                    .message(e.getMessage())
                    .success(false)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.<TopicResponse>builder()
                    .status(500)
                    .message("Internal server error")
                    .success(false)
                    .build());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TopicResponse>> getTopicById(@PathVariable Long id) {
        Topic topic = topicService.getTopicById(id);
        TopicResponse response = topicMapper.toTopicResponse(topic);

        return ResponseEntity.ok(ApiResponse.<TopicResponse>builder()
                .status(200)
                .message("Fetched topic successfully")
                .success(true)
                .data(response)
                .build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTopicById(@PathVariable Long id) {
        topicService.deleteTopicById(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .status(200)
                .message("Topic deleted successfully")
                .success(true)
                .build());
    }

}

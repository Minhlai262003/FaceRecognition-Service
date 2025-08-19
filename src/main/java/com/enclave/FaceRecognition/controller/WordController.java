package com.enclave.FaceRecognition.controller;

import com.enclave.FaceRecognition.dto.Response.ApiResponse;
import com.enclave.FaceRecognition.dto.Response.WordResponse;
import com.enclave.FaceRecognition.entity.Word;
import com.enclave.FaceRecognition.exception.WordNotFoundException;
import com.enclave.FaceRecognition.service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topics/{topicId}/words")
@RequiredArgsConstructor
public class WordController {

    private final WordService wordService;

    // GET all words of a topic
    @GetMapping
    public ResponseEntity<ApiResponse<List<WordResponse>>> getAllWords(@PathVariable Long topicId) {
        try {
            List<WordResponse> words = wordService.getWordsByTopic(topicId);
            return ResponseEntity.ok(ApiResponse.<List<WordResponse>>builder()
                    .status(200)
                    .message("Get all words successfully")
                    .success(true)
                    .data(words)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.<List<WordResponse>>builder()
                    .status(500)
                    .message("Internal server error")
                    .success(false)
                    .build());
        }
    }

    // POST create new word
    @PostMapping
    public ResponseEntity<ApiResponse<WordResponse>> createWord(@PathVariable Long topicId, @RequestBody Word word) {
        try {
            WordResponse created = wordService.createWord(topicId, word);
            return ResponseEntity.status(201).body(ApiResponse.<WordResponse>builder()
                    .status(201)
                    .message("Word created successfully")
                    .success(true)
                    .data(created)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.<WordResponse>builder()
                    .status(400)
                    .message(e.getMessage())
                    .success(false)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.<WordResponse>builder()
                    .status(500)
                    .message("Internal server error")
                    .success(false)
                    .build());
        }
    }

    // PUT update word
    @PutMapping("/{wordId}")
    public ResponseEntity<ApiResponse<WordResponse>> updateWord(
            @PathVariable Long wordId,
            @RequestBody Word word) {
        try {
            WordResponse updated = wordService.updateWord(wordId, word);
            return ResponseEntity.ok(ApiResponse.<WordResponse>builder()
                    .status(200)
                    .message("Word updated successfully")
                    .success(true)
                    .data(updated)
                    .build());
        } catch (WordNotFoundException e) {
            return ResponseEntity.status(404).body(ApiResponse.<WordResponse>builder()
                    .status(404)
                    .message(e.getMessage())
                    .success(false)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.<WordResponse>builder()
                    .status(500)
                    .message("Internal server error")
                    .success(false)
                    .build());
        }
    }

    // DELETE word
    @DeleteMapping("/{wordId}")
    public ResponseEntity<ApiResponse<Void>> deleteWord(@PathVariable Long topicId, @PathVariable Long wordId) {
        try {
            wordService.deleteWord(wordId);
            return ResponseEntity.status(200).body(ApiResponse.<Void>builder()
                    .status(200)
                    .message("Word deleted successfully")
                    .success(true)
                    .build());
            // Nếu muốn chuẩn REST hơn:
            // return ResponseEntity.noContent().build();
        } catch (WordNotFoundException e) {
            return ResponseEntity.status(404).body(ApiResponse.<Void>builder()
                    .status(404)
                    .message(e.getMessage())
                    .success(false)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.<Void>builder()
                    .status(500)
                    .message("Internal server error")
                    .success(false)
                    .build());
        }
    }
}

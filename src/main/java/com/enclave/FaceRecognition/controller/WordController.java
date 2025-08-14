package com.enclave.FaceRecognition.controller;

import com.enclave.FaceRecognition.dto.Response.WordResponse;
import com.enclave.FaceRecognition.entity.Word;
import com.enclave.FaceRecognition.service.WordService;
import org.antlr.v4.runtime.Vocabulary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/words")
public class WordController {

    private final WordService wordService;

    public WordController(WordService vocabularyService) {
        this.wordService = vocabularyService;
    }

    @GetMapping("/topic/{topicId}")
    public ResponseEntity<Map<String, Object>> getByTopicId(@PathVariable Long topicId) {
        List<WordResponse> words = wordService.getByTopicId(topicId);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "Fetched data successfully");
        response.put("success", true);
        response.put("data", words);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/enclave/words/{id}")
    public ResponseEntity<?> deleteVocabulary(@PathVariable String id) {
        try {
            wordService.deleteWordById(id);
            return ResponseEntity.ok(Map.of(
                    "status", 200,
                    "success", true,
                    "message", "Word deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", 500,
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Update word
    @PutMapping("/{id}")
    public WordResponse updateWord(
            @PathVariable Long id,
            @RequestBody WordResponse wordResponse
    ) {
        return wordService.updateWord(id, wordResponse);
    }

}

package com.enclave.FaceRecognition.controller;

import com.enclave.FaceRecognition.dto.Response.ApiResponse;
import com.enclave.FaceRecognition.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/avatar/{filename}")
    public ResponseEntity<ApiResponse<String>> getUserAvatar(@PathVariable String filename) {
        try {
            Resource resource = fileService.loadFileAsResource(filename);

            // build file access URL (example: http://localhost:8080/api/files/avatar/filename.jpg)
            String fileUrl = "/api/files/avatar/" + filename;

            ApiResponse<String> response = ApiResponse.<String>builder()
                    .status(200)
                    .message("Success")
                    .success(true)
                    .data(fileUrl)
                    .build();

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            ApiResponse<String> response = ApiResponse.<String>builder()
                    .status(404)
                    .message("File not found")
                    .success(false)
                    .data(null)
                    .build();

            return ResponseEntity.status(404).body(response);
        }
    }

}



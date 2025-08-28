package com.enclave.FaceRecognition.controller;

import com.enclave.FaceRecognition.dto.Response.ApiResponse;
import com.enclave.FaceRecognition.dto.Response.UnknownFacesData;
import com.enclave.FaceRecognition.service.UnknownService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@Slf4j
@RestController
@RequestMapping("/api/unknown-faces")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Unknown Faces APIs")
public class UnknownController {
    UnknownService unknownService;

    @GetMapping
    public ApiResponse<UnknownFacesData> getUnknownFaces(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(name = "per_page", defaultValue = "20") int perPage) {
        return unknownService.getUnknownFaces(page, perPage);
    }

    @GetMapping("/file/{filename}")
    public ResponseEntity<byte[]> getUnknownFaceImage(@PathVariable String filename) {
        return unknownService.getUnknownFaceImage(filename);
    }
}

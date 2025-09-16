package com.enclave.FaceRecognition.service;

import com.enclave.FaceRecognition.dto.Response.ApiResponse;
import com.enclave.FaceRecognition.dto.Response.PythonResponse;
import com.enclave.FaceRecognition.dto.Response.UnknownFacesData;
import com.enclave.FaceRecognition.exception.AppException;
import com.enclave.FaceRecognition.exception.ErrorCode;
import com.enclave.FaceRecognition.repository.httpclient.PythonClient;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UnknownService {
    PythonClient pythonClient;

    public ApiResponse<UnknownFacesData> getUnknownFaces(int page, int perPage) {
        try {
            PythonResponse<UnknownFacesData> pythonResponse = pythonClient.getUnknownFaces(page, perPage);
            
            // Get data from 'response_data' field from Python response
            UnknownFacesData responseData = pythonResponse.getResponse_data();
            
            // Fix the image URLs to include the context path
            if (responseData != null && responseData.getImages() != null) {
                responseData.getImages().forEach(image -> {
                    if (image.getImage() != null && image.getImage().startsWith("/api/unknown-faces/file/")) {
                        image.setImage("/enclave" + image.getImage());
                    }
                });
            }
            
            return ApiResponse.<UnknownFacesData>builder()
                    .success(pythonResponse.getSuccess())
                    .message(pythonResponse.getMessage())
                    .status(pythonResponse.getStatus())
                    .data(responseData)
                    .build();
        } catch (FeignException e) {
            log.error("Error getting unknown faces: {}", e.getMessage());
            throw new AppException(ErrorCode.PYTHON_SERVICE_ERROR);
        }
    }

    public ResponseEntity<byte[]> getUnknownFaceImage(String filename) {
        try {
            byte[] imageBytes = pythonClient.getUnknownFaceImage(filename);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(imageBytes.length);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(imageBytes);
        } catch (FeignException.NotFound e) {
            log.error("Image not found: {}", filename);
            return ResponseEntity.notFound().build();
        } catch (FeignException e) {
            log.error("Error getting unknown face image: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

package com.enclave.FaceRecognition.repository.httpclient;


import com.enclave.FaceRecognition.configuration.FeignClientConfig;
import com.enclave.FaceRecognition.dto.Request.PythonUserCreationRequest;
import com.enclave.FaceRecognition.dto.Response.PythonResponse;
import com.enclave.FaceRecognition.dto.Response.UnknownFacesData;
import com.enclave.FaceRecognition.dto.Response.UserPythonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "python-service", url = "${app.service.python}", configuration = FeignClientConfig.class)
public interface PythonClient {
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Object registerUser(
            @RequestPart("user_id") String employeeId,
            @RequestPart("name") String name,
            @RequestPart("department") String department,
            @RequestPart("face_images") List<MultipartFile> faceImages
    );
    @DeleteMapping(value = "/users/{user_id}")
    void deleteUser(@PathVariable String user_id);
    @PostMapping(value = "/recognize", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    PythonResponse<UserPythonResponse> recognize(
            @RequestPart("image") MultipartFile fileImage
    );
    
    @GetMapping(value = "/api/unknown-faces")
    PythonResponse<UnknownFacesData> getUnknownFaces(
            @RequestParam("page") int page,
            @RequestParam("per_page") int perPage
    );
    
    @GetMapping(value = "/api/unknown-faces/file/{filename}")
    byte[] getUnknownFaceImage(@PathVariable("filename") String filename);
    @PostMapping(value = "/face-data/{userId}/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    PythonResponse<UserPythonResponse> addFaceUser(@PathVariable("userId") String userId, @RequestPart("image") List<String> images);
}

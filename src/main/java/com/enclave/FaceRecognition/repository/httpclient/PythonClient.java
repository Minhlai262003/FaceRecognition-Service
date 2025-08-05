package com.enclave.FaceRecognition.repository.httpclient;


import com.enclave.FaceRecognition.configuration.FeignClientConfig;
import com.enclave.FaceRecognition.dto.Request.PythonUserCreationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "python-service", url = "${app.service.python}", configuration = FeignClientConfig.class)
public interface PythonClient {
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Object registerUser(
            @RequestPart("employee_id") String employeeId,
            @RequestPart("name") String name,
            @RequestPart("department") String department,
            @RequestPart("face_images") List<MultipartFile> faceImages
    );
    @DeleteMapping(value = "/employees/{employee_id}")
    void deleteUser(@PathVariable String employee_id);
}

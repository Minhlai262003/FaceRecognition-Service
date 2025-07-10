package com.enclave.FaceRecognition.repository.httpclient;


import com.enclave.FaceRecognition.dto.Request.PythonUserCreationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "python-service", url = "${app.service.python}")
public interface PythonClient {
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Object registerUser(@RequestPart PythonUserCreationRequest request,
                                @RequestPart("faceImages") List<MultipartFile> faceImage);
    @DeleteMapping(value = "/employees/{employee_id}")
    void deleteUser(@PathVariable String employee_id);
}

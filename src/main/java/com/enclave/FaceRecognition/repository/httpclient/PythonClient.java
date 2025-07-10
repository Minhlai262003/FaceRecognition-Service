package com.enclave.FaceRecognition.repository.httpclient;


import com.enclave.FaceRecognition.dto.Request.PythonUserCreationRequest;
import com.enclave.FaceRecognition.dto.Response.PythonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "python-service", url = "${app.service.python}")
public interface PythonClient {
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Object registerUser(@RequestPart PythonUserCreationRequest request,
                                @RequestPart("faceImages") List<MultipartFile> faceImage);
}

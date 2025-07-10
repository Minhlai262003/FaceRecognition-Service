package com.enclave.FaceRecognition.service;

import com.enclave.FaceRecognition.dto.Request.PythonUserCreationRequest;
import com.enclave.FaceRecognition.dto.Request.UserCreateRequest;
import com.enclave.FaceRecognition.dto.Response.PythonResponse;
import com.enclave.FaceRecognition.entity.User;
import com.enclave.FaceRecognition.exception.AppException;
import com.enclave.FaceRecognition.exception.ErrorCode;
import com.enclave.FaceRecognition.mapper.UserMapper;
import com.enclave.FaceRecognition.repository.UserRepository;
import com.enclave.FaceRecognition.repository.httpclient.PythonClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PythonClient pythonClient;
    ObjectMapper objectMapper;

    @Transactional
    public void createUser(UserCreateRequest request){
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        User user = userMapper.toUser(request);
        String password = "enclave123";
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(password));
        user.setCreatedAt(LocalDateTime.now());
        User userInfo = userRepository.save(user);

        PythonUserCreationRequest pythonUserCreationRequest = PythonUserCreationRequest.builder()
                .name(userInfo.getLastName())
                .employeeId(userInfo.getId())
                .department(userInfo.getRole())
                .build();
        try {
            var pythonResponse = pythonClient.registerUser(pythonUserCreationRequest, request.getFaceImages());
            log.info("Python response: {}", pythonResponse);
        } catch (FeignException.BadRequest e) {
            log.error("Error setting department: {}", e.getMessage());
            String responseBody = e.contentUTF8();
            String errorMessage = handlePythonServiceResponse(e);
            log.warn("Python service error response: {}", errorMessage);
            throw new AppException(ErrorCode.PYTHON_SERVICE_ERROR);
        }

    }

    public String handlePythonServiceResponse(FeignException.BadRequest e) {
        String errorMessage = "Failed to set department in Python service";
        try {

            String responseBody = e.contentUTF8();

            // Parse JSON response
            JsonNode jsonNode = objectMapper.readTree(responseBody);


            if (jsonNode.has("message")) {
                errorMessage = jsonNode.get("message").asText();
            } else {
                log.warn("No 'message' field found in Python service response");
            }
        } catch (Exception ex) {
            log.error("Error parsing Python error response: {}", ex.getMessage(), ex);
        }
        return errorMessage;
    }
}

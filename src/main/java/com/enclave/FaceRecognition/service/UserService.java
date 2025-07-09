package com.enclave.FaceRecognition.service;

import com.enclave.FaceRecognition.dto.Request.PythonUserCreationRequest;
import com.enclave.FaceRecognition.dto.Request.UserCreateRequest;
import com.enclave.FaceRecognition.entity.User;
import com.enclave.FaceRecognition.exception.AppException;
import com.enclave.FaceRecognition.exception.ErrorCode;
import com.enclave.FaceRecognition.mapper.UserMapper;
import com.enclave.FaceRecognition.repository.UserRepository;
import com.enclave.FaceRecognition.repository.httpclient.PythonClient;
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

    @Transactional
    public void createUser(UserCreateRequest request){
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        User user = userMapper.toUser(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        User userInfo = userRepository.save(user);

        PythonUserCreationRequest pythonUserCreationRequest = PythonUserCreationRequest.builder()
                .name(userInfo.getLastName())
                .employeeId(userInfo.getId())
                .department(userInfo.getRole())
                .build();
        var pythonResponse = pythonClient.registerUser(pythonUserCreationRequest, request.getFaceImages());
        log.info("Python response: {}", pythonResponse);
    }
}

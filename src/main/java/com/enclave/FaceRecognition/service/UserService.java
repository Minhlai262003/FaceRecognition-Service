package com.enclave.FaceRecognition.service;

import com.enclave.FaceRecognition.dto.Request.PythonUserCreationRequest;
import com.enclave.FaceRecognition.dto.Request.UserCreateRequest;
import com.enclave.FaceRecognition.dto.Response.UserResponse;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

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

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber()))
            throw new AppException(ErrorCode.PHONE_EXISTED);

        if (request.getBirthDay() != null) {
            LocalDate birthDate = request.getBirthDay();
            LocalDate now = LocalDate.now();
            int age = Period.between(birthDate, now).getYears();

            if (age < 18) {
                throw new AppException(ErrorCode.AGE_UNDER_18);
            }
        } else {
            throw new AppException(ErrorCode.DATE_OF_BIRTH_REQUIRED);
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
                .department(userInfo.getRole().name())
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


            if (jsonNode.has("status")) {
                errorMessage = jsonNode.get("status").asText();
            } else {
                log.warn("No 'message' field found in Python service response");
            }
        } catch (Exception ex) {
            log.error("Error parsing Python error response: {}", ex.getMessage(), ex);
        }
        return errorMessage;
    }

    @Transactional
    public void deleteUserById(String id) {

        if (!userRepository.existsById(id)) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        log.info("user id deleted: {}", id);
        pythonClient.deleteUser(id);
        userRepository.deleteById(id);

    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

//    public Users updateUser(Long id, UserUpdateDTO dto) {
//        Users existingUser = userRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy user với ID = " + id));
//
//        existingUser.setFirstName(dto.getFirstName());
//        existingUser.setLastName(dto.getLastName());
//        existingUser.setEmail(dto.getEmail());
//        existingUser.setGender(dto.getGender());
//        existingUser.setRole(dto.getRole());
//
//        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
//            existingUser.setPassword(dto.getPassword());
//        }
//
//        return userRepository.save(existingUser);
//    }

    }

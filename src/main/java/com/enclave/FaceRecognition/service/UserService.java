    package com.enclave.FaceRecognition.service;

    import com.enclave.FaceRecognition.dto.Request.PythonUserCreationRequest;
    import com.enclave.FaceRecognition.dto.Request.UserCreateRequest;
    import com.enclave.FaceRecognition.dto.Request.UserUpdateRequest;
    import com.enclave.FaceRecognition.dto.Response.UserResponse;
    import com.enclave.FaceRecognition.entity.Gender;
    import com.enclave.FaceRecognition.entity.Role;
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
    import org.springframework.security.authentication.BadCredentialsException;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;
import com.enclave.FaceRecognition.dto.Request.PythonUserCreationRequest;
import com.enclave.FaceRecognition.dto.Request.UserCreateRequest;
import com.enclave.FaceRecognition.dto.Request.UserUpdateRequest;
import com.enclave.FaceRecognition.dto.Response.PythonResponse;
import com.enclave.FaceRecognition.dto.Response.UserPythonResponse;
import com.enclave.FaceRecognition.dto.Response.UserRecognitionResponse;
import com.enclave.FaceRecognition.dto.Response.UserResponse;
import com.enclave.FaceRecognition.entity.Gender;
import com.enclave.FaceRecognition.entity.Role;
import com.enclave.FaceRecognition.entity.User;
import com.enclave.FaceRecognition.exception.AppException;
import com.enclave.FaceRecognition.exception.DuplicateFieldException;
import com.enclave.FaceRecognition.exception.ErrorCode;
import com.enclave.FaceRecognition.exception.PythonServiceValidationException;
import com.enclave.FaceRecognition.mapper.UserMapper;
import com.enclave.FaceRecognition.mapper.UserRecognitionMapper;
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
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;

    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.time.Period;
    import java.util.List;
    import java.util.Optional;
    import java.util.UUID;

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
            throw new DuplicateFieldException("Email already exists");
        }

            if (userRepository.existsByPhoneNumber(request.getPhoneNumber()))
                throw new AppException(ErrorCode.PHONE_EXISTED);
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber()))
            throw new DuplicateFieldException("Phone number already exists");

            if (request.getBirthDay() != null) {
                LocalDate birthDate = request.getBirthDay();
                LocalDate now = LocalDate.now();
                int age = Period.between(birthDate, now).getYears();

            if (age < 18) {
                throw new AppException(ErrorCode.AGE_UNDER_18);
            }
        }
                if (age < 18) {
                    throw new AppException(ErrorCode.AGE_UNDER_18);
                }
            } else {
                throw new AppException(ErrorCode.DATE_OF_BIRTH_REQUIRED);
            }

            try {
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
            PythonUserCreationRequest pythonUserCreationRequest = PythonUserCreationRequest.builder()
                    .name(userInfo.getFirstName())
                    .employeeId(userInfo.getId())
                    .department(userInfo.getRole().name())
                    .build();


                try {
                    var pythonResponse = pythonClient.registerUser(
                            pythonUserCreationRequest.getEmployeeId(),
                            pythonUserCreationRequest.getName(),
                            pythonUserCreationRequest.getDepartment(),
                            request.getFaceImages()
                    );
                    log.info("Python response: {}", pythonResponse);
                } catch (FeignException.BadRequest e) {
                    log.error("Error setting department: {}", e.getMessage());
                    String responseBody = e.contentUTF8();
                    String errorMessage = handlePythonServiceResponse(e);
                    log.warn("Python service error response: {}", errorMessage);
                    throw new AppException(ErrorCode.PYTHON_SERVICE_ERROR);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException("Error mapping UserCreateRequest to User", ex);
            }
        }

        public String handlePythonServiceResponse(FeignException.BadRequest e) {
            String errorMessage = "Failed to set department in Python service";
            try {
    public PythonResponse<UserRecognitionResponse> recognizeUser(MultipartFile fileImage) {
        try {
            var pythonResponse = pythonClient.recognize(fileImage);
//            if (Boolean.FALSE.equals(pythonResponse.getSuccess())) {
//                throw new PythonServiceValidationException(pythonResponse.getMessage());
//            }
            UserRecognitionResponse user = UserRecognitionMapper.fromPythonResponse(pythonResponse.getUser());
            return PythonResponse.<UserRecognitionResponse>builder()
                    .status(pythonResponse.getStatus())
                    .message(pythonResponse.getMessage())
                    .success(pythonResponse.getSuccess())
                    .user(user)
                    .build();

        } catch (FeignException.BadRequest e) {
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
            UUID uuid = UUID.fromString(id);
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

        public UserResponse getUserById(String id) {
            UUID uuid = UUID.fromString(id);
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return userMapper.toUserResponse(user);
        }

        public void updateUser(String id, UserUpdateRequest userUpdateRequest) {
            User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new BadCredentialsException("User not found"));

            Optional.ofNullable(userUpdateRequest.getFirstName())
                    .ifPresent(existingUser::setFirstName);

            Optional.ofNullable(userUpdateRequest.getLastName())
                    .ifPresent(existingUser::setLastName);

            Optional.ofNullable(userUpdateRequest.getEmail())
                    .ifPresent(existingUser::setEmail);

            Optional.ofNullable(userUpdateRequest.getPhoneNumber())
                    .ifPresent(existingUser::setPhoneNumber);

            Optional.ofNullable(userUpdateRequest.getBirthDay())
                    .ifPresent(existingUser::setBirthDay);

            Optional.ofNullable(userUpdateRequest.getGender())
                    .ifPresent(g -> {
                        try {
                            existingUser.setGender(Gender.valueOf(g.toUpperCase()));
                        } catch (IllegalArgumentException e) {
                            throw new BadCredentialsException("Invalid gender value");
                        }
                    });

            Optional.ofNullable(userUpdateRequest.getRole())
                    .ifPresent(r -> {
                        try {
                            existingUser.setRole(Role.valueOf(r.toUpperCase()));
                        } catch (IllegalArgumentException e) {
                            throw new BadCredentialsException("Invalid role value");
                        }
                    });


             userRepository.save(existingUser);
        }


    }

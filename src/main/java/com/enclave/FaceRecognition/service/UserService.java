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
import com.enclave.FaceRecognition.entity.UserImage;
import com.enclave.FaceRecognition.exception.*;
import com.enclave.FaceRecognition.entity.UserImage;
import com.enclave.FaceRecognition.exception.*;
import com.enclave.FaceRecognition.mapper.UserMapper;
import com.enclave.FaceRecognition.mapper.UserRecognitionMapper;
import com.enclave.FaceRecognition.repository.UserImageRepository;
import com.enclave.FaceRecognition.repository.UserImageRepository;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {
    final UserRepository userRepository;
    final UserMapper userMapper;
    final PythonClient pythonClient;
    final ObjectMapper objectMapper;
    final UserImageRepository userImageRepository;
    @Value("${file.upload-dir}")
    String uploadDir;
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
//            User userCurrent = userRepository.findById(user.getUserID()).orElseThrow(() ->  new UserNotFoundException("User not found"));
//            user.setUserName(userCurrent.getFirstName());
//            User userCurrent = userRepository.findById(user.getUserID()).orElseThrow(() ->  new UserNotFoundException("User not found"));
//            user.setUserName(userCurrent.getFirstName());
            return PythonResponse.<UserRecognitionResponse>builder()
                    .status(pythonResponse.getStatus())
                    .message(pythonResponse.getMessage())
                    .success(pythonResponse.getSuccess())
                    .user(user)
                    .build();


        } catch (FeignException.NotFound e) {
            return PythonResponse.<UserRecognitionResponse>builder()
                    .status(e.status())
                    .message("No faces detected in the image")
                    .success(false)
                    .build();
        }catch (FeignException e) {
            if (e.status() == 411){
                return PythonResponse.<UserRecognitionResponse>builder()
                        .status(e.status())
                        .message("No faces detected in the image")
                        .success(false)
                        .build();
            }
            if (e.status() == 400){
                return PythonResponse.<UserRecognitionResponse>builder()
                        .status(e.status())
                        .message("Face(s) detected but not recognized")
                        .success(false)
                        .build();
            }
            return PythonResponse.<UserRecognitionResponse>builder()
                    .status(e.status())
                    .message(e.getMessage())
                    .success(false)
                    .build();

        } catch (FeignException.NotFound e) {
            return PythonResponse.<UserRecognitionResponse>builder()
                    .status(e.status())
                    .message("No faces detected in the image")
                    .success(false)
                    .build();
        }catch (FeignException e) {
            if (e.status() == 411){
                return PythonResponse.<UserRecognitionResponse>builder()
                        .status(e.status())
                        .message("No faces detected in the image")
                        .success(false)
                        .build();
            }
            if (e.status() == 400){
                return PythonResponse.<UserRecognitionResponse>builder()
                        .status(e.status())
                        .message("Face(s) detected but not recognized")
                        .success(false)
                        .build();
            }
            return PythonResponse.<UserRecognitionResponse>builder()
                    .status(e.status())
                    .message(e.getMessage())
                    .success(false)
                    .build();
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
        try{
            if (!userRepository.existsById(id)) {
                throw new AppException(ErrorCode.USER_NOT_EXISTED);
            }
            pythonClient.deleteUser(id);
            userRepository.deleteById(id);
        @Transactional
        public void deleteUserById(String id) {
            UUID uuid = UUID.fromString(id);
            if (!userRepository.existsById(id)) {
                throw new AppException(ErrorCode.USER_NOT_EXISTED);
            }
            log.info("user id deleted: {}", id);
            pythonClient.deleteUser(id);
            userRepository.deleteById(id);

        }catch (FeignException.NotFound e) {
            userRepository.deleteById(id);
        }
    }
        }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAllByVerifiedOrderByFirstNameAsc(true);

        return users.stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    public List<UserResponse> getAllUsersNotVerified() {
        List<User> users = userRepository.findAllByVerified(false);
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
    @Transactional
    public void updateUser(String id, UserUpdateRequest userUpdateRequest) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new BadCredentialsException("User not found"));
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

    @Transactional
    public void selfCreateUser(UserCreateRequest userCreateRequest) {
        if(userCreateRequest.isVerified()){
            throw new BadRequestException("The verified field have to be false");
        }
        if(userRepository.existsByEmail(userCreateRequest.getEmail())) {
            throw new DuplicateFieldException("Email already exists");
        }

        if (userRepository.existsByPhoneNumber(userCreateRequest.getPhoneNumber()))
            throw new DuplicateFieldException("Phone number already exists");

        if (userCreateRequest.getBirthDay() != null) {
            LocalDate birthDate = userCreateRequest.getBirthDay();
            LocalDate now = LocalDate.now();
            int age = Period.between(birthDate, now).getYears();

            if (age < 18) {
                throw new AppException(ErrorCode.AGE_UNDER_18);
            }
        }

        try {
            User user = userMapper.toUser(userCreateRequest);
            String password = "enclave123";
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            user.setPassword(passwordEncoder.encode(password));
            user.setCreatedAt(LocalDateTime.now());
            userRepository.save(user);

            File uploadDirFile = new File(uploadDir).getAbsoluteFile();
            if (!uploadDirFile.exists()) {
                boolean created = uploadDirFile.mkdirs();
                if (!created) {
                    throw new RuntimeException("Cannot create upload directory: " + uploadDirFile.getAbsolutePath());
                }
            }

            for (MultipartFile file : userCreateRequest.getFaceImages()) {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                String filePath = uploadDir + "/" + fileName;

                File dest = new File(filePath);
                dest.getParentFile().mkdirs();
                file.transferTo(dest);

                // Lưu path vào DB
                UserImage userImage = UserImage.builder()
                        .path(filePath)
                        .user(user)
                        .build();

                userImageRepository.save(userImage);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error mapping UserCreateRequest to User", ex);
        }
    }
    @Transactional
    public void refuseCreateUser(String userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new BadRequestException("User not found"));

            List<UserImage> listUserImages = userImageRepository.findByUserId(user.getId());

            for(UserImage userImage : listUserImages){
                File file = new File(userImage.getPath());
                if(file.exists()){
                    boolean delete = file.delete();
                    if(!delete){
                        System.err.println("Failed to delete file: " + file.getAbsolutePath());
                    }
                }
            }
            userImageRepository.deleteAll(listUserImages);
            userRepository.deleteById(user.getId());
        }catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error refusing user with id: " + userId, ex);
        }
    }

    public void verifiedCreateUser(String userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new BadRequestException("User not found"));

            List<UserImage> listUserImages = userImageRepository.findByUserId(user.getId());
            List<MultipartFile> listFaceImages = new ArrayList<>();
            for (UserImage userImage : listUserImages) {
                File file = new File(userImage.getPath());
                if (file.exists() && file.isFile()) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        String fileName = file.getName();
                        String contentType = "application/octet-stream"; // fallback

                        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                            contentType = "image/jpeg";
                        } else if (fileName.endsWith(".png")) {
                            contentType = "image/png";
                        } else if (fileName.endsWith(".gif")) {
                            contentType = "image/gif";
                        }

                        MultipartFile multipartFile = new MockMultipartFile(
                                fileName,
                                fileName,
                                contentType,
                                fis
                        );
                        listFaceImages.add(multipartFile);

                    } catch (IOException e) {
                        log.error("Failed to read file: {}", file.getAbsolutePath(), e);
                    }
                } else {
                    log.warn("File not found or not a file: {}", userImage.getPath());
                }
            }

            if (!listFaceImages.isEmpty()) {
                PythonUserCreationRequest pythonRequest = PythonUserCreationRequest.builder()
                        .name(user.getFirstName())
                        .employeeId(user.getId())
                        .department(user.getRole().name())
                        .build();

                try {
                    var pythonResponse = pythonClient.registerUser(
                            pythonRequest.getEmployeeId(),
                            pythonRequest.getName(),
                            pythonRequest.getDepartment(),
                            listFaceImages
                    );
                    log.info("Python response: {}", pythonResponse);
                    for (UserImage userImage : listUserImages) {
                        File file = new File(userImage.getPath());
                        if (file.exists() && file.isFile()) {
                            boolean deleted = file.delete();
                            if (!deleted) {
                                log.warn("Failed to delete file: {}", file.getAbsolutePath());
                            }
                        }
                    }
                    userImageRepository.deleteAll(listUserImages);
                    user.setVerified(true);
                    user.setActive(true);
                    userRepository.save(user);
                } catch (FeignException.BadRequest e) {
                    String errorMessage = handlePythonServiceResponse(e);
                    log.warn("Python service error response: {}", errorMessage);
                    throw new AppException(ErrorCode.PYTHON_SERVICE_ERROR);
                }
            }

        } catch (Exception ex) {
            log.error("Error creating verified user files for userId: {}", userId, ex);
            throw new RuntimeException("Error creating verified user files for userId: " + userId, ex);
        }
    }
}

    }

package com.enclave.FaceRecognition.mapper;

import com.enclave.FaceRecognition.dto.Request.UserCreateRequest;
import com.enclave.FaceRecognition.dto.Response.UserResponse;
import com.enclave.FaceRecognition.entity.Gender;
import com.enclave.FaceRecognition.entity.Role;
import com.enclave.FaceRecognition.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "gender", expression = "java(this.mapGender(request.getGender()))")
    @Mapping(target = "role", expression = "java(this.mapRole(request.getRole()))")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "birthDay", target = "birthDay")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "verified", source = "verified")
    User toUser(UserCreateRequest request);

    @Mapping(source = "active", target = "isActive")
    @Mapping(source = "verified", target = "isVerified")
    UserResponse toUserResponse(User user);

    default Gender mapGender(String gender) {
        try {
            return gender == null ? null : Gender.valueOf(gender.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid gender value: " + gender);
        }
    }

    default Role mapRole(String role) {
        try {
            return role == null ? null : Role.valueOf(role.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role value: " + role);
        }
    }
}


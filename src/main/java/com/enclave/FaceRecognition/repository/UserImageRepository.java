package com.enclave.FaceRecognition.repository;

import com.enclave.FaceRecognition.entity.User;
import com.enclave.FaceRecognition.entity.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {
    List<UserImage> findByUserId(String userId);
}

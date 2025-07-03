package com.enclave.FaceRecognition.service;

import com.enclave.FaceRecognition.exception.UserNotFoundException;
import com.enclave.FaceRecognition.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Không tìm thấy user với id = " + id);
        }
        userRepository.deleteById(id);
    }
}


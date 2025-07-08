package com.enclave.FaceRecognition.service;

import com.enclave.FaceRecognition.entity.Users;
import com.enclave.FaceRecognition.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.parser.Entity;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String pythonApiBaseUrl = "http://localhost:5000/api"; // URL Flask

    public void deleteUserById(Long id) {
        // Kiểm tra user có tồn tại không
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy user với id = " + id);
        }

        // Xoá ảnh trên Python
        try {
            String url = pythonApiBaseUrl + "/delete-image/" + id;
            restTemplate.delete(url);
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("Ảnh không tồn tại bên Flask: " + e.getMessage());
        } catch (HttpClientErrorException e) {
            System.out.println("Lỗi từ Flask khi xoá ảnh: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Lỗi không xác định khi gọi Flask API: " + e.getMessage());
        }

        // Xoá user trong DB
        userRepository.deleteById(id);
    }


}
    
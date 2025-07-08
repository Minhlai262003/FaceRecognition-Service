package com.enclave.FaceRecognition.service;

import com.enclave.FaceRecognition.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String pythonApiBaseUrl = "http://localhost:5000/api"; // Flask API base URL

    public void deleteUserById(Long id) {
        // 1. Kiểm tra user có tồn tại
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy user với ID = " + id);
        }

        // 2. Gọi Flask API để xóa ảnh
        String flaskUrl = pythonApiBaseUrl + "/delete-image/" + id;
        try {
            restTemplate.delete(flaskUrl);
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Không tìm thấy ảnh bên Flask để xoá: {}", e.getMessage());
        } catch (HttpClientErrorException e) {
            log.error("Lỗi từ Flask khi xoá ảnh: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Lỗi không xác định khi gọi Flask: {}", e.getMessage());
        }

        // 3. Xoá user trong DB
        userRepository.deleteById(id);
        log.info("Đã xoá user với ID = {}", id);
    }
}

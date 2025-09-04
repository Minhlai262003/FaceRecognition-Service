package com.enclave.FaceRecognition.service;

import com.enclave.FaceRecognition.dto.Response.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    @Value("${openweathermap.api.key}")
    private String apiKey;

    private final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    public WeatherResponse getWeatherByCity(String city) {
        String url = BASE_URL + "?q=" + city + "&appid=" + apiKey + "&units=metric";

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, WeatherResponse.class);
    }
}

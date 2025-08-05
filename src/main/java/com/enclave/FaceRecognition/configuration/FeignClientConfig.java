package com.enclave.FaceRecognition.configuration;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class FeignClientConfig {

    private final Dotenv dotenv;

    public FeignClientConfig(Dotenv dotenv) {
        this.dotenv = dotenv;
    }

    @Bean
    public RequestInterceptor bearerAuthInterceptor() {
        String apiKey = dotenv.get("API_KEY");
        return template -> {
            template.header("x-api-key", apiKey);
        };
    }
}


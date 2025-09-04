package com.enclave.FaceRecognition.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse {
    private List<Weather> weather;
    private Main main;
    private Wind wind;
    private Clouds clouds;
    private long dt;
    private int timezone;
    private String name;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Weather {
        private int id;
        private String main;
        private String description;
        private String icon;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Main {
        private double temp;
        private int humidity;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Wind {
        private double speed;
        private int deg;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Clouds {
        private int all;
    }


}

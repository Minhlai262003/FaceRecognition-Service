package com.enclave.FaceRecognition.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeId;

public enum Gender {
    male,female,other;
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Gender fromString(String value) {
        if (value == null) return null;
        return Gender.valueOf(value.trim().toUpperCase());
    }

}

package com.enclave.FaceRecognition.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class InvalidatedToken {
    @Id
    String id;
    String expiryTime;
}

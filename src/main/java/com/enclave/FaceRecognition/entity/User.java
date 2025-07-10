package com.enclave.FaceRecognition.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String email;
    String firstName;
    String lastName;
    String phoneNumber;
    LocalDate birthDay;
    String gender;
    String password;
    String role;
    LocalDateTime createdAt;
}

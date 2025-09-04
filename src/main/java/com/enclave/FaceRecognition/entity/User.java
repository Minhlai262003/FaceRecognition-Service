package com.enclave.FaceRecognition.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    @Enumerated(EnumType.STRING)
    Gender gender;
    String password;
    @Enumerated(EnumType.STRING)
    Role role;
    @Access(AccessType.FIELD)
    @Column(name = "is_active")
    Boolean active;
    @Access(AccessType.FIELD)
    @Column(name = "is_verified")
    Boolean verified;
    @CreationTimestamp
    LocalDateTime createdAt;
    @UpdateTimestamp
    LocalDateTime updatedAt;

}


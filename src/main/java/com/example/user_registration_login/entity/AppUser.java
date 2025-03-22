package com.example.user_registration_login.entity;

import com.example.user_registration_login.validations.EmailValidator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Table(name = "app_user")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(unique = true, nullable = false)
    @EmailValidator(message = "Please provide a valid email address")
    private String email;
    @Column(nullable = false)
    private String password;
    private String mobileNumber;
    @CreationTimestamp
    private LocalDateTime created_at;
    private String status;

}

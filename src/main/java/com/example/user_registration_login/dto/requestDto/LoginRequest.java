package com.example.user_registration_login.dto.requestDto;

import com.example.user_registration_login.validations.EmailValidator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @EmailValidator(message = "Please provide valid email address")
    private String email;
    private String password;
}

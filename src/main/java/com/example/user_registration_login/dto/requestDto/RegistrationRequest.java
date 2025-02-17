package com.example.user_registration_login.dto.requestDto;

import com.example.user_registration_login.validations.EmailValidator;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {
    private String firstName;
    private String lastName;
    @EmailValidator(message = "Please provide a valid email address")
    private String email;
    private String password;
    private String mobileNumber;
}

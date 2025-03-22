package com.example.user_registration_login.dto.responceDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginResponse {
private String token;
private String refreshToken;
private Object userObj;
}

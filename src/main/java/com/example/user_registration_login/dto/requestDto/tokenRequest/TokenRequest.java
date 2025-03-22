package com.example.user_registration_login.dto.requestDto.tokenRequest;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class TokenRequest {
    private String email;
    @Builder.Default
    LocalDateTime now = LocalDateTime.now();
}

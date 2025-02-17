package com.example.user_registration_login.dto.responceDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseResponse<T> {

    private String code;
    private String title;
    private String message;
    private T data;
}

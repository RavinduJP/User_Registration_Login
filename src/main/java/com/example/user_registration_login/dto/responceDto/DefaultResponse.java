package com.example.user_registration_login.dto.responceDto;

import com.example.user_registration_login.utils.ResponseCodeUtils;
import com.example.user_registration_login.utils.ResponseUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@Builder
public class DefaultResponse {

    private String code;
    private String status;
    private String message;
    private Object data;

    public DefaultResponse() {
    }

    public DefaultResponse(String code, String status, String message, Object data) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public DefaultResponse(String code, String status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = new HashMap<>();
    }

    public static DefaultResponse success(String status, String message, Object data) {
        return new DefaultResponse(ResponseCodeUtils.SUCCESS_CODE, ResponseUtils.SUCCESS, message, data);
    }

    public static DefaultResponse success(String status, String message) {
        return new DefaultResponse(ResponseCodeUtils.SUCCESS_CODE, status, message, new HashMap<String, Object>());
    }
    public static DefaultResponse error(String status, String message, Object data) {
        return new DefaultResponse(ResponseCodeUtils.FAILED_CODE, ResponseUtils.FAILED, message, data);
    }

    public static DefaultResponse error(String status, String message) {
        return new DefaultResponse(ResponseCodeUtils.FAILED_CODE, status, message, new HashMap<String, Object>());
    }
    public static DefaultResponse internalServerError(String status, String message, Object data) {
        return new DefaultResponse(ResponseCodeUtils.INTERNAL_SERVER_ERROR_CODE, ResponseUtils.INTERNAL_SERVER_ERROR, message, data);
    }

    public static DefaultResponse internalServerError(String status, String message) {
        return new DefaultResponse(ResponseCodeUtils.INTERNAL_SERVER_ERROR_CODE, status, message, new HashMap<String, Object>());
    }
}

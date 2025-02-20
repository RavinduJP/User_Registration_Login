package com.example.user_registration_login.controller;

import com.example.user_registration_login.dto.requestDto.RegistrationRequest;
import com.example.user_registration_login.dto.responceDto.BaseResponse;
import com.example.user_registration_login.dto.responceDto.DefaultResponse;
import com.example.user_registration_login.entity.AppUser;
import com.example.user_registration_login.service.impl.UserServiceImpl;
import com.example.user_registration_login.utils.ResponseCodeUtils;
import com.example.user_registration_login.utils.ResponseUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getHello() {
        return "hello world!";
    }

    @PostMapping("/register")
    public ResponseEntity<DefaultResponse> registerUsers(@Valid @RequestBody RegistrationRequest registrationRequest) {
        log.info("User registration attempted for email: {}", registrationRequest.getEmail());
        try {
            BaseResponse<HashMap<String, Object>> response = userService.registerNewUser(registrationRequest);
            if (response.getCode().equals(ResponseCodeUtils.SUCCESS_CODE)) {
                return ResponseEntity.ok(DefaultResponse.success(ResponseUtils.SUCCESS, response.getMessage(), response.getData()));
            } else if (response.getCode().equals(ResponseCodeUtils.INTERNAL_SERVER_ERROR_CODE)) {
                return ResponseEntity.internalServerError()
                        .body(DefaultResponse.internalServerError(ResponseCodeUtils.INTERNAL_SERVER_ERROR_CODE, response.getMessage()));
            } else {
                return ResponseEntity.badRequest()
                        .body(DefaultResponse.error(ResponseUtils.FAILED, response.getMessage(), response.getData()));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(DefaultResponse.error(ResponseUtils.FAILED, e.getMessage(),null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(DefaultResponse.internalServerError(ResponseCodeUtils.INTERNAL_SERVER_ERROR_CODE, "Unexpected error occurred"));
        }
    }

    @GetMapping("/AllUsers")
    public List<AppUser> getAllUsers() {
        return userService.getAllUsers();
    }

}

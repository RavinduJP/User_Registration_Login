package com.example.user_registration_login.service;

import com.example.user_registration_login.dto.requestDto.RegistrationRequest;
import com.example.user_registration_login.dto.responceDto.BaseResponse;
import com.example.user_registration_login.entity.AppUser;

import java.util.HashMap;
import java.util.List;

public interface UserService {

    public List<AppUser> getAllUsers();

    BaseResponse<HashMap<String, Object>> registerNewUser(RegistrationRequest registrationRequest);
}

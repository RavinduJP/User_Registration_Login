package com.example.user_registration_login.service.impl;

import com.example.user_registration_login.dto.requestDto.RegistrationRequest;
import com.example.user_registration_login.dto.responceDto.BaseResponse;
import com.example.user_registration_login.entity.AppUser;
import com.example.user_registration_login.repository.UserRepository;
import com.example.user_registration_login.service.UserService;
import com.example.user_registration_login.utils.ResponseCodeUtils;
import com.example.user_registration_login.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    public final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    public BaseResponse<HashMap<String, Object>> registerNewUser(RegistrationRequest registrationRequest) {
        try {
//            AppUser newUser = userRepository.findByEmail(registrationRequest.getEmail());
            if (userRepository.existsByEmail(registrationRequest.getEmail())) {
                return BaseResponse.<HashMap<String, Object>>builder()
                        .code(ResponseCodeUtils.FAILED_CODE)
                        .title(ResponseUtils.FAILED)
                        .message("User already exists with email: " + registrationRequest.getEmail())
                        .build();
            }

            AppUser newUser = AppUser.builder()
                    .firstName(registrationRequest.getFirstName())
                    .lastName(registrationRequest.getLastName())
                    .email(registrationRequest.getEmail())
                    .password(passwordEncoder.encode(registrationRequest.getPassword()))
                    .mobileNumber(registrationRequest.getMobileNumber())
                    .created_at(LocalDateTime.now())
                    .build();

//            if (newUser == null) {
//                return BaseResponse.<HashMap<String, Object>>builder()
//                        .code(ResponseCodeUtils.FAILED_CODE)
//                        .title(ResponseUtils.FAILED)
//                        .message("User not found " + registrationRequest.getEmail())
//                        .build();
//            }

            AppUser savedUser = userRepository.save(newUser);

            HashMap<String, Object> newUserObj = new HashMap<>();
            newUserObj.put("firstName", savedUser.getFirstName());
            newUserObj.put("lastName", savedUser.getLastName());
            newUserObj.put("email", savedUser.getEmail());
            newUserObj.put("password", savedUser.getPassword());
            newUserObj.put("mobileNumber", savedUser.getMobileNumber());
            newUserObj.put("createdAt", savedUser.getCreated_at());

            return BaseResponse.<HashMap<String, Object>>builder()
                    .code(ResponseCodeUtils.SUCCESS_CODE)
                    .title(ResponseUtils.SUCCESS)
                    .message("User creation successfully")
                    .build();

        } catch (Exception e) {
            return BaseResponse.<HashMap<String, Object>>builder()
                    .code(ResponseCodeUtils.INTERNAL_SERVER_ERROR_CODE)
                    .title(ResponseUtils.INTERNAL_SERVER_ERROR)
                    .message("Error occurred while saving new user details")
                    .build();

        }
    }


}

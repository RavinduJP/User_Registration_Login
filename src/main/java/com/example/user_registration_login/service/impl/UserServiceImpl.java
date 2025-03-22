package com.example.user_registration_login.service.impl;

import com.example.user_registration_login.dto.requestDto.LoginRequest;
import com.example.user_registration_login.dto.requestDto.RegistrationRequest;
import com.example.user_registration_login.dto.requestDto.tokenRequest.TokenRequest;
import com.example.user_registration_login.dto.responceDto.BaseResponse;
import com.example.user_registration_login.dto.responceDto.LoginResponse;
import com.example.user_registration_login.entity.AppUser;
import com.example.user_registration_login.repository.UserRepository;
import com.example.user_registration_login.service.UserService;
import com.example.user_registration_login.utils.JwtUtils;
import com.example.user_registration_login.utils.ResponseCodeUtils;
import com.example.user_registration_login.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static com.example.user_registration_login.enums.Status.ACTIVE;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    public final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    @Override
    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    public BaseResponse<HashMap<String, Object>> registerNewUser(RegistrationRequest registrationRequest) {
        try {
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

            AppUser savedUser = userRepository.save(newUser);

            HashMap<String, Object> newUserObj = new HashMap<>();
            newUserObj.put("firstName", savedUser.getFirstName());
            newUserObj.put("lastName", savedUser.getLastName());
            newUserObj.put("email", savedUser.getEmail());
            newUserObj.put("password", savedUser.getPassword());
            newUserObj.put("mobileNumber", savedUser.getMobileNumber());
            newUserObj.put("createdAt", savedUser.getCreated_at());
            newUserObj.put("status", savedUser.getStatus());

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


    public BaseResponse<LoginResponse> login(LoginRequest loginRequest) {
        try {
            if (loginRequest == null || loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
                log.error("Invalid login request: {}", loginRequest);
                return BaseResponse.<LoginResponse>builder()
                        .code(ResponseCodeUtils.FAILED_CODE)
                        .title(ResponseUtils.FAILED)
                        .message("Invalid login request")
                        .build();
            }

            String password = loginRequest.getPassword();
            log.info("The password is : {}", loginRequest.getPassword());
            AppUser loginUser = userRepository.findByEmail(loginRequest.getEmail());
            if (loginUser == null) {
                log.warn("There is No User Found -> {}", loginRequest.getEmail());
                return BaseResponse.<LoginResponse>builder()
                        .code(ResponseCodeUtils.FAILED_CODE)
                        .title(ResponseUtils.FAILED)
                        .message("App User Not Found")
                        .build();
            }
            return logUser(loginRequest, password, loginUser);
        } catch (Exception e) {
            log.error("user SignUp -> Exception : {}", e.getMessage(), e);
            return BaseResponse.<LoginResponse>builder()
                    .code(ResponseCodeUtils.INTERNAL_SERVER_ERROR_CODE)
                    .title(ResponseUtils.INTERNAL_SERVER_ERROR)
                    .message("Error occurred while user login")
                    .build();
        }
    }

    private BaseResponse<LoginResponse> logUser(LoginRequest loginRequest, String password, AppUser loginUser) {
        try {
            if (!ACTIVE.name().equals(loginUser.getStatus())) {
                log.info("logUser -> Disabled or inactive user");
                return BaseResponse.<LoginResponse>builder()
                        .code(ResponseCodeUtils.DISABLE_USER_ERROR_CODE)
                        .title(ResponseUtils.FAILED)
                        .message("User is not active. Please contact the Support center.")
                        .build();
            }
            log.info("Authenticating user: {}", loginUser.getEmail());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), password));

            TokenRequest tokenRequest = TokenRequest.builder()
                    .email(loginUser.getEmail())
                    .now(LocalDateTime.now())
                    .build();

            String token = jwtUtils.generateToken(tokenRequest);
            String refreshToken = jwtUtils.refreshToken(tokenRequest);
            String getUsername = jwtUtils.getUsernameFromToken(token);

            LoginResponse response = LoginResponse.builder()
                    .token(token)
                    .refreshToken(refreshToken)
                    .userObj(AppUser.builder()
                            .firstName(loginUser.getFirstName())
                            .lastName(loginUser.getLastName())
                            .email(loginUser.getEmail())
                            .mobileNumber(loginUser.getMobileNumber())
                            .status(loginUser.getStatus())
                            .build())
                    .build();

            return BaseResponse.<LoginResponse>builder()
                    .code(ResponseCodeUtils.SUCCESS_CODE)
                    .title(ResponseUtils.SUCCESS)
                    .message("User Logged in Successfully.")
                    .data(response)
                    .build();
        } catch (BadCredentialsException e) {
            log.info("LogUser -> Invalid credentials for user: {}", loginUser.getEmail());
            return BaseResponse.<LoginResponse>builder()
                    .code(ResponseCodeUtils.FAILED_CODE)
                    .title(ResponseUtils.FAILED)
                    .message("Invalid username or password")
                    .build();
        } catch (Exception e) {
            log.error("Unexpected error during login: {}", e.getMessage(), e);
            return BaseResponse.<LoginResponse>builder()
                    .code(ResponseCodeUtils.INTERNAL_SERVER_ERROR_CODE)
                    .title(ResponseUtils.INTERNAL_SERVER_ERROR)
                    .message("An error occurred during login")
                    .build();
        }
    }

}

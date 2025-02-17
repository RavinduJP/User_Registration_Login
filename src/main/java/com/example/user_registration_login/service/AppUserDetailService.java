package com.example.user_registration_login.service;

import com.example.user_registration_login.entity.AppUser;
import com.example.user_registration_login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AppUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByEmail(email);
            if (appUser == null) {
                throw new UsernameNotFoundException("No user found with username: " + email);
            }
            if (userRepository.existsByEmail(appUser.getEmail())) {
                throw new IllegalArgumentException("The User Already Exists!");
            }
        return new org.springframework.security.core.userdetails.User(
                appUser.getEmail(),
                appUser.getPassword(),
                Collections.emptyList()
        );
    }
}

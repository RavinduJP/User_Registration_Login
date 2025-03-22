package com.example.user_registration_login.repository;

import com.example.user_registration_login.entity.AppUser;
import org.apache.catalina.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByEmail(String email);
    AppUser findAppUserByEmail(String email);

    boolean existsByEmail(String email);

//    Optional<AppUser> findByUsername(String email);
}

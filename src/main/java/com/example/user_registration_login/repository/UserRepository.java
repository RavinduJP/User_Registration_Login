package com.example.user_registration_login.repository;

import com.example.user_registration_login.entity.AppUser;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByEmail(String email);

    boolean existsByEmail(String email);
}

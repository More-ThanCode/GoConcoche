package com.more_than_code.go_con_coche.registered_user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegisteredUserRepository extends JpaRepository <RegisteredUser, Long> {
    Optional<RegisteredUser> findByUsername(String username);
    Optional<RegisteredUser> findByEmail(String email);
}

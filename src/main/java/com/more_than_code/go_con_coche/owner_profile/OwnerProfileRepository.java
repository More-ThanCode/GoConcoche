package com.more_than_code.go_con_coche.owner_profile;

import com.more_than_code.go_con_coche.registered_user.RegisteredUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerProfileRepository extends JpaRepository<OwnerProfile, Long> {
    boolean findByRegisteredUserId(RegisteredUser registeredUser);
    Optional<OwnerProfile> findByRegisteredUserId(Long registeredUserId);
}
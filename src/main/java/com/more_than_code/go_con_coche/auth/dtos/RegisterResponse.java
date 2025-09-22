package com.more_than_code.go_con_coche.auth.dtos;
import com.more_than_code.go_con_coche.role.Role;
import java.util.Set;

public record RegisterResponse(Long id,
                               String username,
                               Set<Role> roles) {
}
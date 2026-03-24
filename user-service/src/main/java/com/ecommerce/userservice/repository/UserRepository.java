package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByEmail(@NotBlank String email);
}

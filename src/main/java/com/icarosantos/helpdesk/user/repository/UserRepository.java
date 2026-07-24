package com.icarosantos.helpdesk.user.repository;

import com.icarosantos.helpdesk.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
}

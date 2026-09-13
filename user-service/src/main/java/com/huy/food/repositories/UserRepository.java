package com.huy.food.repositories;

import com.huy.food.entities.User;
import com.huy.food.enums.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByPhone(String phone);

    boolean existsByPhone(String phone);

    boolean existsByIdAndRole(UUID userId, AccountRole role);

    boolean existsByIdAndBanned(UUID userId, boolean banned);
}
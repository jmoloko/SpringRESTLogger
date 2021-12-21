package com.milk.restfilelogger.repository;

import com.milk.restfilelogger.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Jack Milk
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByName(String username);
    Optional<UserEntity> findByEmail(String email);
}

package com.milk.restfilelogger.repository;

import com.milk.restfilelogger.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author Jack Milk
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByName(String username);
    UserEntity getByEmail(String email);
    Optional<UserEntity> findByEmail(String email);
    @Query("SELECT u.id FROM UserEntity as u WHERE u.email = ?1")
    Long getIdByEmail(String email);
}

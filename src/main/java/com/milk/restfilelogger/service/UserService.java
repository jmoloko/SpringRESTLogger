package com.milk.restfilelogger.service;

import com.milk.restfilelogger.entity.UserEntity;
import com.milk.restfilelogger.exception.UserAlreadyExistException;
import com.milk.restfilelogger.exception.UserNotFoundException;

import java.util.List;

/**
 * @author Jack Milk
 */
public interface UserService {
    List<UserEntity> getAll();
    Long getIdByEmail(String email) throws UserNotFoundException;
    UserEntity getById(Long id) throws UserNotFoundException;
    UserEntity getByEmail(String email) throws UserNotFoundException;
    UserEntity update(UserEntity user, Long id) throws UserAlreadyExistException, UserNotFoundException;
    UserEntity save(UserEntity user) throws UserAlreadyExistException;
    UserEntity delete(Long id) throws UserNotFoundException, UserAlreadyExistException;
}

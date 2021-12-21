package com.milk.restfilelogger.service;

import com.milk.restfilelogger.dto.UserDTO;
import com.milk.restfilelogger.entity.UserEntity;
import com.milk.restfilelogger.exception.UserAlreadyExistException;
import com.milk.restfilelogger.exception.UserNotFoundException;

import java.util.List;

/**
 * @author Jack Milk
 */
public interface UserService {
    List<UserDTO> getAll();
    UserDTO getById(Long id) throws UserNotFoundException;
    UserDTO update(UserEntity user, Long id) throws UserAlreadyExistException, UserNotFoundException;
    UserDTO registration(UserEntity user) throws UserAlreadyExistException;
    Long delete(Long id);
}

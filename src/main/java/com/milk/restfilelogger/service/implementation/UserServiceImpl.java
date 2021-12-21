package com.milk.restfilelogger.service.implementation;

import com.milk.restfilelogger.dto.UserDTO;
import com.milk.restfilelogger.entity.UserEntity;
import com.milk.restfilelogger.exception.UserAlreadyExistException;
import com.milk.restfilelogger.exception.UserNotFoundException;
import com.milk.restfilelogger.repository.UserRepository;
import com.milk.restfilelogger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jack Milk
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserDTO> getAll() {
        return userRepository.findAll().stream().map(UserDTO::toDto).collect(Collectors.toList());
    }

    @Override
    public UserDTO getById(Long id) throws UserNotFoundException {
        return UserDTO.toDto(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User NOT Found")));
    }

    @Override
    public UserDTO update(UserEntity user, Long id) throws UserAlreadyExistException, UserNotFoundException {
        UserEntity uUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User NOT Found"));
        if (userRepository.findByName(user.getName()) != null ){
            throw new UserAlreadyExistException("User already exist!");
        }

        uUser.setName(user.getName());
        uUser.setRole(user.getRole());
        return UserDTO.toDto(userRepository.save(uUser));
    }

    @Override
    public UserDTO registration(UserEntity user) throws  UserAlreadyExistException{
        if (userRepository.findByName(user.getName()) != null ){
            throw new UserAlreadyExistException("User already exist!");
        }
        return UserDTO.toDto(userRepository.save(user));
    }

    @Override
    public Long delete(Long id) {
        userRepository.deleteById(id);
        return id;
    }
}

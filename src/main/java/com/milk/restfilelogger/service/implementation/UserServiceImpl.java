package com.milk.restfilelogger.service.implementation;

import com.milk.restfilelogger.dto.UserDTO;
import com.milk.restfilelogger.entity.Status;
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

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Long getIdByEmail(String email) throws UserNotFoundException {
        Long id = userRepository.getIdByEmail(email);
        if (id == null) {
            throw new UserNotFoundException("User NOT found");
        }
        return id;
    }

    @Override
    public UserEntity getById(Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User NOT Found"));
    }

    @Override
    public UserEntity getByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(()-> new UserNotFoundException("User NOT Found"));
    }

    @Override
    public UserEntity update(UserEntity updatedUser, Long id) throws UserAlreadyExistException, UserNotFoundException {
        UserEntity currentUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User NOT Found"));
        if (userRepository.findByEmail(updatedUser.getEmail()).isPresent() && !id.equals(currentUser.getId())){
            throw new UserAlreadyExistException("User already exist!");
        }

        return userRepository.save(updatedUser);
    }

    @Override
    public UserEntity save(UserEntity user) throws  UserAlreadyExistException{
        if (userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new UserAlreadyExistException("User already exist!");
        }
        return userRepository.save(user);
    }

    @Override
    public UserEntity delete(Long id) throws UserNotFoundException, UserAlreadyExistException {
        UserEntity deletedUser =  userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User NOT Found"));
        deletedUser.setStatus(Status.DELETED);
        return update(deletedUser, id);
    }
}

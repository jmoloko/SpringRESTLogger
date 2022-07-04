package com.milk.restfilelogger.service.implementation;

import com.milk.restfilelogger.entity.Status;
import com.milk.restfilelogger.entity.UserEntity;
import com.milk.restfilelogger.exception.UserAlreadyExistException;
import com.milk.restfilelogger.exception.UserNotFoundException;
import com.milk.restfilelogger.repository.UserRepository;
import com.milk.restfilelogger.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jack Milk
 */

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserEntity> getAll() {
        log.info("Fetching all users ");
        return userRepository.findAll();
    }

    @Override
    public Long getIdByEmail(String email) throws UserNotFoundException {
        log.info("Fetching user ID by email: {}", email);
        Long id = userRepository.getIdByEmail(email);
        if (id == null) {
            log.error("User with id {} was not found", id);
            throw new UserNotFoundException("User NOT found");
        }
        return id;
    }

    @Override
    public UserEntity getById(Long id) throws UserNotFoundException {
        log.info("Fetching user by id {}", id);
        return userRepository.findById(id).orElseThrow(() -> {
            log.error("User with id {} not found", id);
            return new UserNotFoundException("User NOT Found");
        });
    }

    @Override
    public UserEntity getByEmail(String email) throws UserNotFoundException {
        log.info("Fetching user by email {}", email);
        return userRepository.findByEmail(email).orElseThrow(()-> {
            log.error("User with email {} not found", email);
            return new UserNotFoundException("User NOT Found");
        });
    }

    @Override
    public UserEntity update(UserEntity updatedUser, Long id) throws UserAlreadyExistException, UserNotFoundException {
        log.info("Update user {}", updatedUser.getName());
        UserEntity currentUser = userRepository.findById(id).orElseThrow(() -> {
            log.error("User by id {} not found", id);
            return new UserNotFoundException("User NOT Found");
        });

        log.info("Check updated user for the existence of email in the database");
        if (userRepository.findByEmail(updatedUser.getEmail()).isPresent() && !id.equals(currentUser.getId())){
            log.error("User with email {} already exist", updatedUser.getEmail());
            throw new UserAlreadyExistException("User already exist!");
        }

        return userRepository.save(updatedUser);
    }

    @Override
    public UserEntity save(UserEntity user) throws  UserAlreadyExistException{
        log.info("Saving new user: {}", user.getName());
        if (userRepository.findByEmail(user.getEmail()).isPresent()){
            log.error("User by email {} already exist!", user.getEmail());
            throw new UserAlreadyExistException("User already exist!");
        }
        return userRepository.save(user);
    }

    @Override
    public UserEntity delete(Long id) throws UserNotFoundException, UserAlreadyExistException {
        log.info("Delete user by id {}", id);
        UserEntity deletedUser =  userRepository.findById(id).orElseThrow(() -> {
            log.error("User with id {} not found", id);
            return new UserNotFoundException("User NOT Found");
        });
        deletedUser.setStatus(Status.DELETED);
        return update(deletedUser, id);
    }
}

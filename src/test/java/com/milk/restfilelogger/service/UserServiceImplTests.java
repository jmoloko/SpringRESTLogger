package com.milk.restfilelogger.service;

import com.milk.restfilelogger.entity.Role;
import com.milk.restfilelogger.entity.Status;
import com.milk.restfilelogger.entity.UserEntity;
import com.milk.restfilelogger.exception.UserAlreadyExistException;
import com.milk.restfilelogger.exception.UserNotFoundException;
import com.milk.restfilelogger.repository.UserRepository;
import com.milk.restfilelogger.service.implementation.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Jack Milk
 */

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity getUser(){
        UserEntity newUser = new UserEntity();
        newUser.setId(1L);
        newUser.setEmail("johndoe@yahoo.com");
        newUser.setName("JohnDoe");
        newUser.setRole(Role.ADMIN);
        newUser.setStatus(Status.ACTIVE);
        return newUser;
    }

    private UserEntity getNewUser(){
        UserEntity newUser = new UserEntity();
        newUser.setId(1L);
        newUser.setEmail("johndoe@yahoo.com");
        newUser.setName("John Doe");
        newUser.setRole(Role.MODERATOR);
        newUser.setStatus(Status.DELETED);
        return newUser;
    }

    private List<UserEntity> getUsers(){
        List<UserEntity> users = Stream.of(
            new UserEntity("johndoe@yahoo.com", "JohnDoe", Role.ADMIN, Status.ACTIVE),
            new UserEntity("mikesnow@mail.com", "MikeSnow", Role.MODERATOR, Status.ACTIVE),
            new UserEntity("testUser@gmail.com", "TestUser", Role.USER, Status.ACTIVE)
        ).collect(Collectors.toList());
        return users;
    }

    @Test
    public void getAllUsersTest() {
        when(userRepository.findAll()).thenReturn(getUsers());
        assertEquals(3, userService.getAll().size());
        assertEquals("JohnDoe", userService.getAll().get(0).getName());
        assertEquals("johndoe@yahoo.com", userService.getAll().get(0).getEmail());
        assertEquals("ADMIN", userService.getAll().get(0).getRole().name());
        assertEquals("ACTIVE", userService.getAll().get(0).getStatus().name());

        verify(userRepository, times(5)).findAll();
    }

    @Test
    public void getUserByIdTest() throws UserNotFoundException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(getUser()));
        assertEquals(1L, userService.getById(1L).getId());
        assertEquals("JohnDoe", userService.getById(1L).getName());
        assertEquals("johndoe@yahoo.com", userService.getById(1L).getEmail());
        assertEquals("ADMIN", userService.getById(1L).getRole().name());
        assertEquals("ACTIVE", userService.getById(1L).getStatus().name());

        verify(userRepository, times(5)).findById(1L);
    }

    @Test
    public void getUserByEmail() throws UserNotFoundException {
        when(userRepository.findByEmail("johndoe@yahoo.com")).thenReturn(Optional.of(getUser()));
        assertEquals(1L, userService.getByEmail("johndoe@yahoo.com").getId());
        assertEquals("JohnDoe", userService.getByEmail("johndoe@yahoo.com").getName());
        assertEquals("ADMIN", userService.getByEmail("johndoe@yahoo.com").getRole().name());
        assertEquals("ACTIVE", userService.getByEmail("johndoe@yahoo.com").getStatus().name());

        verify(userRepository, times(4)).findByEmail("johndoe@yahoo.com");
    }

    @Test
    public void getIdByUserEmailTest() throws UserNotFoundException {
        when(userRepository.getIdByEmail("johndoe@yahoo.com")).thenReturn(getUser().getId());
        assertEquals(1L, userService.getIdByEmail("johndoe@yahoo.com"));

        verify(userRepository, times(1)).getIdByEmail("johndoe@yahoo.com");
    }

    @Test
    public void updateUserTest() {
        /**
         * TODO:
         * Implement updateUserTest
         */
    }

    @Test
    public void saveUserTest() throws UserAlreadyExistException {
        when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(getUser());
        assertEquals("JohnDoe", userService.save(getUser()).getName());
        assertEquals("johndoe@yahoo.com", userService.save(getUser()).getEmail());
        assertEquals("ADMIN", userService.save(getUser()).getRole().name());
        assertEquals("ACTIVE", userService.save(getUser()).getStatus().name());

        verify(userRepository, times(4)).save(Mockito.any(UserEntity.class));
    }

    @Test
    public void deleteUserTest() {
        /**
         * TODO:
         * Implement deleteUserTest
         */
    }
}

package com.milk.restfilelogger.rest.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.milk.restfilelogger.entity.Role;
import com.milk.restfilelogger.entity.Status;
import com.milk.restfilelogger.entity.UserEntity;
import com.milk.restfilelogger.security.JwtTokenProvider;
import com.milk.restfilelogger.service.implementation.EventServiceImpl;
import com.milk.restfilelogger.service.implementation.FileServiceImpl;
import com.milk.restfilelogger.service.implementation.UserServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Jack Milk
 */

@SpringBootTest
@AutoConfigureMockMvc
public class ExecutiveRestControllerV1Test {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    UserServiceImpl userService;
    @MockBean
    FileServiceImpl fileService;
    @MockBean
    EventServiceImpl eventService;


    private UserEntity getUser(Long id, String name, String email, Role role, Status status){
        UserEntity newUser = new UserEntity();
        newUser.setId(id);
        newUser.setEmail(email);
        newUser.setName(name);
        newUser.setRole(role);
        newUser.setStatus(status);
        return newUser;
    }

    private String getToken(String email, Role role) {
        return jwtTokenProvider.createToken(email, role.name());
    }

    UserEntity user_1 = getUser(1L, "JohnDoe", "johndoe@yahoo.com", Role.ADMIN, Status.ACTIVE);
    UserEntity user_2 = getUser(2L, "MikeSnow", "mikesnow@mail.com", Role.MODERATOR, Status.ACTIVE);
    UserEntity user_3 = getUser(3L, "TestUser", "testuser@gmail.com", Role.USER, Status.ACTIVE);

//    @Test
//    public void shouldNotAllowAccessToUnauthenticatedUsers() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/executive/users")).andExpect(status().isForbidden());
//    }
//
//    @Test
//    public void shouldNotAllowAccessToEndpointExecutive() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/executive/users")
//                .header("Authorization", getToken("user@gmail.com", Role.USER)))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    public void shouldAllowAccessToEndpointExecutive() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/executive/users")
//                .header("Authorization", getToken("johndoe@yahoo.com", Role.ADMIN)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void getAllUsers_success() throws Exception {
//        List<UserEntity> users = Stream.of(user_1, user_2, user_3).collect(Collectors.toList());
//
//        Mockito.when(userService.getAll()).thenReturn(users);
//
//        mockMvc.perform(MockMvcRequestBuilders
//                .get("/api/v1/executive/users")
//                .header("Authorization", getToken("johndoe@yahoo.com", Role.ADMIN)))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("JohnDoe")));
//    }
//
//    @Test
//    public void getUserById_success() throws Exception {
//
//        Mockito.when(userService.getById(1L)).thenReturn(user_1);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/executive/users/1")
//                .header("Authorization", getToken("johndoe@yahoo.com", Role.ADMIN)))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("name", Matchers.is("JohnDoe")));
//    }



}

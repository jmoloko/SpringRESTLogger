package com.milk.restfilelogger.rest.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.milk.restfilelogger.entity.Role;
import com.milk.restfilelogger.entity.Status;
import com.milk.restfilelogger.entity.UserEntity;
import com.milk.restfilelogger.service.EventService;
import com.milk.restfilelogger.service.FileService;
import com.milk.restfilelogger.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Jack Milk
 */

@WebMvcTest(ExecutiveRestControllerV1.class)
public class ExecutiveRestControllerV1Test {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;
    @MockBean
    FileService fileService;
    @MockBean
    EventService eventService;

    private UserEntity getUser(Long id, String name, String email, Role role, Status status){
        UserEntity newUser = new UserEntity();
        newUser.setId(id);
        newUser.setEmail(email);
        newUser.setName(name);
        newUser.setRole(role);
        newUser.setStatus(status);
        return newUser;
    }

    UserEntity user_1 = getUser(1L, "JohnDoe", "johndoe@yahoo.com", Role.ADMIN, Status.ACTIVE);
    UserEntity user_2 = getUser(2L, "MikeSnow", "mikesnow@mail.com", Role.MODERATOR, Status.ACTIVE);
    UserEntity user_3 = getUser(3L, "TestUser", "testuser@gmail.com", Role.USER, Status.ACTIVE);

    private String obtainAccessToken(String email, String password) throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("email", email);
        params.add("password", password);

        ResultActions result
                = mockMvc.perform(post("/api/v1/auth/login")
                .params(params)
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("token").toString();
    }


    @Test
    public void getAllUsers_success() throws Exception {
        List<UserEntity> users = Stream.of(user_1, user_2, user_3).collect(Collectors.toList());
        String accessToken = obtainAccessToken("johndoe@yahoo.com", "admin");

        Mockito.when(userService.getAll()).thenReturn(users);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/executive/users")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("JohnDoe")));
    }
}

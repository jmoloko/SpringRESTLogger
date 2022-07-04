package com.milk.restfilelogger.rest.v1;

import com.fasterxml.jackson.annotation.JsonView;
import com.milk.restfilelogger.dto.CredentialRequestDTO;
import com.milk.restfilelogger.dto.JsonViews;
import com.milk.restfilelogger.entity.UserEntity;
import com.milk.restfilelogger.repository.UserRepository;
import com.milk.restfilelogger.security.JwtTokenProvider;
import com.milk.restfilelogger.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Jack Milk
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthRestControllerV1 {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final Utils utils;

    @PostMapping("/registration")
    @JsonView(JsonViews.FullView.class)
    public ResponseEntity<?> registration(@RequestBody CredentialRequestDTO requestDTO) {
        return utils.saveNewUser(requestDTO);
    }

    @PostMapping("/login")
    @JsonView(JsonViews.ShortView.class)
    public ResponseEntity<?> authenticate(@RequestBody CredentialRequestDTO request){

        try {
            String email = request.getEmail();
            String password = request.getPassword();
            UserEntity userEntity = userRepository.getByEmail(email);

            if (userEntity == null || userEntity.getStatus().name().equals("DELETED")){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            String token = jwtTokenProvider.createToken(email, userEntity.getRole().name());

            Map<Object, Object> response = new HashMap<>();
            response.put("email", email);
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid email or password", HttpStatus.FORBIDDEN);
        }

    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }

}

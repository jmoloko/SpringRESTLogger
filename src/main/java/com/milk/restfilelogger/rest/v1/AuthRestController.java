package com.milk.restfilelogger.rest.v1;

import com.milk.restfilelogger.dto.AuthRequestDTO;
import com.milk.restfilelogger.dto.RegisterRequestDTO;
import com.milk.restfilelogger.dto.UserDTO;
import com.milk.restfilelogger.entity.Role;
import com.milk.restfilelogger.entity.Status;
import com.milk.restfilelogger.entity.UserEntity;
import com.milk.restfilelogger.exception.UserAlreadyExistException;
import com.milk.restfilelogger.repository.UserRepository;
import com.milk.restfilelogger.security.JwtTokenProvider;
import com.milk.restfilelogger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jack Milk
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthRestController(AuthenticationManager authenticationManager, UserRepository userRepository, UserService userService, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody RegisterRequestDTO requestDTO) {

        String email = requestDTO.getEmail();
        String name = requestDTO.getName();
        String password = requestDTO.getPassword();

        String regexEmail = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        String regexName = "^[A-Za-z_][A-Za-z0-9_]{4,29}$";
        String regexPassword = "^[A-Za-z_][A-Za-z0-9_]{3,29}$";

        if (!email.matches(regexEmail)) {
            return  new ResponseEntity<>("Invalid email", HttpStatus.FORBIDDEN);
        }
        if (!name.matches(regexName)) {
            return  new ResponseEntity<>("Invalid name", HttpStatus.FORBIDDEN);
        }
        if (!password.matches(regexPassword)) {
            return  new ResponseEntity<>("Invalid password", HttpStatus.FORBIDDEN);
        }

        UserEntity newUser = new UserEntity();
        newUser.setEmail(email);
        newUser.setName(name);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRole(Role.USER);
        newUser.setStatus(Status.ACTIVE);

        UserEntity savedUser;
        try {
            savedUser = userService.save(newUser);
        } catch (UserAlreadyExistException e) {
            e.printStackTrace();
            return new ResponseEntity<>("User already exists!", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(UserDTO.toDto(savedUser), HttpStatus.OK);

    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequestDTO request){

        try {
            String email = request.getEmail();
            String password = request.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
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

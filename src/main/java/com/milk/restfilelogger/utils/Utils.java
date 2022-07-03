package com.milk.restfilelogger.utils;

import com.milk.restfilelogger.dto.CredentialRequestDTO;
import com.milk.restfilelogger.dto.UserDTO;
import com.milk.restfilelogger.entity.*;
import com.milk.restfilelogger.exception.FileAlreadyExistException;
import com.milk.restfilelogger.exception.UserAlreadyExistException;
import com.milk.restfilelogger.service.EventService;
import com.milk.restfilelogger.service.FileService;
import com.milk.restfilelogger.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Getter
public class Utils {

    private final UserService userService;
    private final FileService fileService;
    private final EventService eventService;
    private final PasswordEncoder passwordEncoder;

    @Value("${upload.path}")
    private String defaultPath;

    public ResponseEntity<?> saveNewUser(CredentialRequestDTO newUser) {
        String name = newUser.getName();
        String email = newUser.getEmail();
        String password = newUser.getPassword();

        String regexEmail = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        String regexName = "^[A-Za-z0-9_]*\\s*[A-Za-z0-9_]{3,29}$";
        String regexPassword = "^[A-Za-z_]*[A-Za-z0-9_]{3,29}$";

        if (!email.matches(regexEmail)) {
            return  new ResponseEntity<>("Invalid email", HttpStatus.FORBIDDEN);
        }
        if (!name.matches(regexName)) {
            return  new ResponseEntity<>("Invalid name", HttpStatus.FORBIDDEN);
        }
        if (!password.matches(regexPassword)) {
            return  new ResponseEntity<>("Invalid password", HttpStatus.FORBIDDEN);
        }

        UserEntity nUser = new UserEntity();
        nUser.setEmail(email);
        nUser.setName(name);
        nUser.setPassword(passwordEncoder.encode(password));
        nUser.setRole(Role.USER);
        nUser.setStatus(Status.ACTIVE);

        UserEntity savedUser;
        try {
            savedUser = userService.save(nUser);
        } catch (UserAlreadyExistException e) {
            e.printStackTrace();
            return new ResponseEntity<>("User already exists!", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(UserDTO.toDto(savedUser), HttpStatus.OK);
    }

    public ResponseEntity<?> uploadFile(UserEntity user, MultipartFile file) throws IOException, FileAlreadyExistException {

        String fileName = file.getOriginalFilename();
        String uploadPath = defaultPath + File.separator + user.getName().replace(" ", "");

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()){
            uploadDir.mkdir();
        }
        file.transferTo(new File(uploadPath + File.separator + fileName));

        FileEntity newFile = new FileEntity(fileName, uploadPath);
        EventEntity newEvent = new EventEntity(user, newFile, Occasion.UPLOAD);
        fileService.save(newFile);
        eventService.save(newEvent);
        return ResponseEntity.ok("File upload successfully.");
    }
}

package com.milk.restfilelogger.rest.v1;

import com.fasterxml.jackson.annotation.JsonView;
import com.milk.restfilelogger.dto.*;
import com.milk.restfilelogger.entity.*;
import com.milk.restfilelogger.exception.FileAlreadyExistException;
import com.milk.restfilelogger.exception.FileNotFoundException;
import com.milk.restfilelogger.exception.UserAlreadyExistException;
import com.milk.restfilelogger.exception.UserNotFoundException;
import com.milk.restfilelogger.service.EventService;
import com.milk.restfilelogger.service.FileService;
import com.milk.restfilelogger.service.UserService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jack Milk
 */

@RestController
@RequestMapping(value = "/api/v1/executive")
public class ExecutiveRestControllerV1 {

    private final UserService userService;
    private final FileService fileService;
    private final EventService eventService;
    private final PasswordEncoder passwordEncoder;
    @Value("${upload.path}")
    private String defaultPath;

    @Autowired
    public ExecutiveRestControllerV1(UserService userService, FileService fileService, EventService eventService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.fileService = fileService;
        this.eventService = eventService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    @JsonView(UserViews.FullView.class)
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(userService.getAll()
                .stream()
                .map(UserDTO::toDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping("/users")
    @PreAuthorize("hasAuthority('level:high')")
    public ResponseEntity<?> saveNewUser(@RequestBody RegisterRequestDTO newUser) {
        String name = newUser.getName();
        String email = newUser.getEmail();
        String password = newUser.getPassword();

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

    @GetMapping("/users/{id}")
    @JsonView(UserViews.FullView.class)
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(UserDTO.toDto(userService.getById(id)), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateStatusForUserById(@PathVariable Long id,
                                                     @RequestBody UserEntity userEntity) {

        try {
            UserEntity currentUser = userService.getById(id);

            if (currentUser.getRole() == Role.MODERATOR || currentUser.getRole() == Role.ADMIN) {
                return new ResponseEntity<>("Not enough rights", HttpStatus.METHOD_NOT_ALLOWED);
            }

            currentUser.setStatus(userEntity.getStatus());
            return new ResponseEntity<>(UserDTO.toDto(userService.update(currentUser, id)), HttpStatus.OK);
        } catch (UserNotFoundException | UserAlreadyExistException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/users/{id}")
    @PreAuthorize("hasAuthority('level:high')")
    public ResponseEntity<?> updateUserById(@PathVariable Long id,
                                            @RequestBody UserEntity userEntity) {

        try {
            UserEntity currentUser = userService.getById(id);

            if (currentUser.getRole() == Role.ADMIN) {
                return new ResponseEntity<>("Not enough rights", HttpStatus.METHOD_NOT_ALLOWED);
            }

            currentUser.setEmail(userEntity.getEmail());
            currentUser.setName(userEntity.getName());
            currentUser.setRole(userEntity.getRole());
            currentUser.setStatus(userEntity.getStatus());

            return new ResponseEntity<>(UserDTO.toDto(userService.update(currentUser, id)), HttpStatus.OK);
        } catch (UserNotFoundException | UserAlreadyExistException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAuthority('level:high')")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {

        try {
            UserEntity currentUser = userService.getById(id);
            List<FileEntity> currentFiles = fileService.getFilesByUserId(id);
            List<EventEntity> currentEvents = eventService.getEventsByUserId(id);

            for (FileEntity f: currentFiles) {
                fileService.delete(f.getId());
            }

            for (EventEntity e: currentEvents) {
                eventService.delete(e.getId());
            }

            String pathToDeletingFiles = defaultPath + File.separator + currentUser.getName().replace(" ", "");
            File delDir = new File(pathToDeletingFiles);
            FileUtils.deleteDirectory(delDir);

            return new ResponseEntity<>(UserDTO.toDto(userService.delete(id)), HttpStatus.OK);
        } catch (UserNotFoundException | UserAlreadyExistException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/users/{id}/files")
    public ResponseEntity<?> getFilesForUser(@PathVariable Long id) {

        return new ResponseEntity<>(fileService.getFilesByUserId(id)
                .stream()
                .map(FileDTO::toDto)
                .collect(Collectors.toList())
                , HttpStatus.OK);
    }

    @PostMapping("/users/{id}/files")
    public ResponseEntity<?> uploadFileFromUser(@PathVariable Long id, @RequestParam("file") MultipartFile file) {

        if (file != null) {

            try {
                String fileName = file.getOriginalFilename();
                UserEntity user = userService.getById(id);
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

            } catch (UserNotFoundException e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } catch (FileAlreadyExistException | IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }
        return new ResponseEntity<>("No file content",  HttpStatus.NO_CONTENT);

    }

    @GetMapping("/users/{id}/events")
    public ResponseEntity<?> getEventsByUserId(@PathVariable Long id) {

        return new ResponseEntity<>(eventService.getEventsByUserId(id)
                .stream()
                .map(EventDTO::toDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/users/{uId}/files/{fId}")
    public ResponseEntity<?> getFileById(@PathVariable Long uId,  @PathVariable Long fId) {

        try {
            if (fileService.getFilesByUserId(uId).stream().noneMatch(f -> f.getId().equals(fId))) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
                return new ResponseEntity<>(FileDTO.toDto(fileService.getFileById(fId)), HttpStatus.OK);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/users/{uId}/files/{fId}/download")
    public ResponseEntity<?> downloadFileById(@PathVariable Long uId, @PathVariable Long fId) {

        try {
            if (fileService.getFilesByUserId(uId).stream().noneMatch(f -> f.getId().equals(fId))) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            FileEntity currentFile = fileService.getFileById(fId);

            String pathToFile = currentFile.getPath() + File.separator + currentFile.getName();
            Path path = Paths.get(pathToFile);
            Resource resource = new UrlResource(path.toUri());

            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf(MediaType.MULTIPART_MIXED_VALUE))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (FileNotFoundException | MalformedURLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/users/{uId}/files/{fId}")
    public ResponseEntity<?> deleteFileById(@PathVariable Long uId, @PathVariable Long fId) {

        try {
            if (fileService.getFilesByUserId(uId).stream().noneMatch(f -> f.getId().equals(fId))) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            FileEntity file = fileService.getFileById(fId);
            UserEntity user = userService.getById(uId);
            String fullPath = file.getPath() + File.separator + file.getName();
            File delFile = new File(fullPath);

            if (delFile.delete()) {
                fileService.delete(fId);
                eventService.save(new EventEntity(user, file, Occasion.DELETE));
                return new ResponseEntity<>("File deleting successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (FileNotFoundException | UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/users/{uId}/files/{fId}/events")
    public ResponseEntity<?> getEventsByFileId(@PathVariable Long uId, @PathVariable Long fId) {

        if (fileService.getFilesByUserId(uId).stream().noneMatch(f -> f.getId().equals(fId))) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(eventService.getEventsByFileId(fId)
                .stream()
                .map(EventDTO::toDto)
                .collect(Collectors.toList())
                , HttpStatus.OK);
    }
}

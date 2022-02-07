package com.milk.restfilelogger.rest.v1;

import com.fasterxml.jackson.annotation.JsonView;
import com.milk.restfilelogger.dto.*;
import com.milk.restfilelogger.entity.EventEntity;
import com.milk.restfilelogger.entity.FileEntity;
import com.milk.restfilelogger.entity.Occasion;
import com.milk.restfilelogger.entity.UserEntity;
import com.milk.restfilelogger.exception.FileAlreadyExistException;
import com.milk.restfilelogger.exception.FileNotFoundException;
import com.milk.restfilelogger.exception.UserNotFoundException;
import com.milk.restfilelogger.service.EventService;
import com.milk.restfilelogger.service.FileService;
import com.milk.restfilelogger.service.UserService;
import com.milk.restfilelogger.service.implementation.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * @author Jack Milk
 */

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserRestControllerV1 {

    private final UserService userService;
    private final FileService fileService;
    private final EventService eventService;
    @Value("${upload.path}")
    private String defaultPath;

    @Autowired
    public UserRestControllerV1(UserServiceImpl userService, FileService fileService, EventService eventService) {
        this.userService = userService;
        this.fileService = fileService;
        this.eventService = eventService;
    }


    @GetMapping
    @JsonView(UserViews.ShortView.class)
    public ResponseEntity<?> getUserById(Authentication authentication) {
        String name = authentication.getName();

        UserEntity user;
        try {
            user = userService.getByEmail(name);
            UserDTO result = UserDTO.toDto(user);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/files")
    public ResponseEntity<?> getFilesByUser(Authentication authentication) {
        String name = authentication.getName();

        Long userId;
        try {
            userId = userService.getIdByEmail(name);
            return new ResponseEntity<>(
                    fileService.getFilesByUserId(userId)
                            .stream()
                            .map(FileDTO::toDto)
                            .collect(Collectors.toList()),
                    HttpStatus.OK);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/files")
    public ResponseEntity<?> newFile(@RequestParam("file")MultipartFile file, Authentication authentication) {
        String name = authentication.getName();


        UserEntity user;
        try {
            if (file != null) {
                String fileName = file.getOriginalFilename();
                user = userService.getByEmail(name);
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
            return new ResponseEntity<>("No file content",  HttpStatus.NO_CONTENT);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (FileAlreadyExistException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/files/{id}")
    public ResponseEntity<?> getFileById(@PathVariable Long id, Authentication authentication) {
        String name = authentication.getName();

        Long userId;
        try {
            userId = userService.getIdByEmail(name);

            if (fileService.getFilesByUserId(userId).stream().anyMatch(f -> f.getId().equals(id))) {
                return new ResponseEntity<>(FileDTO.toDto(fileService.getFileById(id)), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (UserNotFoundException | FileNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/files/{id}/events")
    public ResponseEntity<?> getEventsFromFile(@PathVariable Long id, Authentication authentication){
        String name = authentication.getName();

        Long userId;
        try {
            userId = userService.getIdByEmail(name);

            if (fileService.getFilesByUserId(userId).stream().anyMatch(f -> f.getId().equals(id))) {
                return new ResponseEntity<>(eventService.getEventsByFileId(id)
                        .stream()
                        .map(EventDTO::toDto)
                        .collect(Collectors.toList())
                        , HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping(value = "/files/{id}/download")
    public ResponseEntity<?> downloadFileByID(@PathVariable Long id, Authentication authentication) {
        String name = authentication.getName();

        UserEntity user;
        Long userId;
        try {
            user = userService.getByEmail(name);
            userId = user.getId();
            FileEntity currentFile = fileService.getFileById(id);

            if (fileService.getFilesByUserId(userId).stream().anyMatch(f -> f.getId().equals(id))) {
                String pathToFile = currentFile.getPath() + File.separator + currentFile.getName();
                Path path = Paths.get(pathToFile);
                Resource resource = new UrlResource(path.toUri());

                eventService.save(new EventEntity(user, currentFile, Occasion.DOWNLOAD));
                return ResponseEntity.ok()
                        .contentType(MediaType.valueOf(MediaType.MULTIPART_MIXED_VALUE))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        } catch (UserNotFoundException | FileNotFoundException | MalformedURLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/files/{id}")
    public ResponseEntity<?> renameFileById(@PathVariable Long id,
                                            @RequestBody FileEntity fileEntity,
                                            Authentication authentication) {
        String uName = authentication.getName();

        UserEntity user;
        Long userId;
        try {
            user = userService.getByEmail(uName);
            userId = user.getId();

            if (fileService.getFilesByUserId(userId).stream().anyMatch(f -> f.getId().equals(id))) {
                String path = fileService.getFileById(id).getPath();
                String oldFileName = fileService.getFileById(id).getName();

                FileEntity currentFile = fileService.getFileById(id);
                currentFile.setName(fileEntity.getName());

                FileEntity newFile = fileService.renameFile(currentFile);
                String newFileName = newFile.getName();

                File currentFileName = new File(path + File.separator + oldFileName);
                File nFileName = new File(path + File.separator + newFileName);

                if (currentFileName.renameTo(nFileName)){
                    eventService.save(new EventEntity(user, currentFile, Occasion.RENAME));
                    return new ResponseEntity<>(FileDTO.toDto(newFile), HttpStatus.OK);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }

            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (UserNotFoundException | FileNotFoundException | FileAlreadyExistException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/files/{id}")
    public ResponseEntity<?> deleteFileById(@PathVariable Long id, Authentication authentication) {
        String name = authentication.getName();

        UserEntity user;
        Long userId;
        try {
            user = userService.getByEmail(name);
            userId = user.getId();

            if (fileService.getFilesByUserId(userId).stream().anyMatch(f -> f.getId().equals(id))) {

                FileEntity file = fileService.getFileById(id);
                String fullPath = file.getPath() + File.separator + file.getName();
                File delFile = new File(fullPath);

                if (delFile.delete()) {
                    fileService.delete(id);
                    eventService.save(new EventEntity(user, file, Occasion.DELETE));
                    return new ResponseEntity<>("File deleting successfully", HttpStatus.OK);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }

            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (UserNotFoundException | FileNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/events")
    public ResponseEntity<?> getEventsByUser(Authentication authentication) {
        String name = authentication.getName();

        Long userId;
        try {
            userId = userService.getIdByEmail(name);

            return new ResponseEntity<>(eventService.getEventsByUserId(userId)
                    .stream()
                    .map(EventDTO::toDto)
                    .collect(Collectors.toList()), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }


}

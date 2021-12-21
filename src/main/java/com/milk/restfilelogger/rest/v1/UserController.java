package com.milk.restfilelogger.rest.v1;

import com.milk.restfilelogger.entity.UserEntity;
import com.milk.restfilelogger.exception.FileNotFoundException;
import com.milk.restfilelogger.exception.UserAlreadyExistException;
import com.milk.restfilelogger.exception.UserNotFoundException;
import com.milk.restfilelogger.service.EventService;
import com.milk.restfilelogger.service.FileService;
import com.milk.restfilelogger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Jack Milk
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private EventService eventService;

    @GetMapping()
    public ResponseEntity getUsers() {
        try {
            return ResponseEntity.ok(userService.getAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getUserById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(userService.getById(id));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @GetMapping("/{id}/files")
    public ResponseEntity getFilesByUserId(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(fileService.getFilesByUserId(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @GetMapping("/{userId}/files/{fileId}")
    public ResponseEntity getFileByUserId(@PathVariable("userId") Long userId,
                                          @PathVariable("fileId") Long fileId) {

        try {
            return ResponseEntity.ok(fileService.getFilesByUserId(userId)
                    .stream()
                    .filter(f -> f.getId().equals(fileId))
                    .findFirst()
                    .orElseThrow(() -> new FileNotFoundException("File Not Found")));
        } catch (FileNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @GetMapping("/{userId}/files/{fileId}/events")
    public ResponseEntity getEventsByUserAndFileId(@PathVariable("userId") Long userId,
                                                   @PathVariable("fileId") Long fileId) {

        try {
            return ResponseEntity.ok(eventService.getEventsByFileId(fileId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }

    }

    @GetMapping("/{id}/events")
    public ResponseEntity getEventsByUserId(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(eventService.getEventsByUserId(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @PostMapping
    public ResponseEntity registration(@RequestBody UserEntity user) {
        try {
            return ResponseEntity.ok(userService.registration(user));
        } catch (UserAlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateUser(@PathVariable("id") Long id, @RequestBody UserEntity user) {
        try {
            return ResponseEntity.ok(userService.update(user, id));
        } catch (UserAlreadyExistException | UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUserById(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(userService.delete(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }
}

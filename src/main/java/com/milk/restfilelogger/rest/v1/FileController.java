package com.milk.restfilelogger.rest.v1;

import com.milk.restfilelogger.exception.FileNotFoundException;
import com.milk.restfilelogger.exception.UserNotFoundException;
import com.milk.restfilelogger.service.EventService;
import com.milk.restfilelogger.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jack Milk
 */
@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity getFiles() {
        try {
            return ResponseEntity.ok(fileService.getAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getFileById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(fileService.getFileById(id));
        } catch (FileNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @GetMapping("/{id}/events")
    public ResponseEntity getEventsByFileId(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(eventService.getEventsByFileId(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

}

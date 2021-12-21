package com.milk.restfilelogger.rest.v1;

import com.milk.restfilelogger.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jack Milk
 */
@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    @Autowired
    EventService eventService;

    @GetMapping
    public ResponseEntity getEvents() {
        try {
            return ResponseEntity.ok(eventService.getAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }
}

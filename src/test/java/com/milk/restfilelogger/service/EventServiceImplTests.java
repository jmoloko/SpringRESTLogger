package com.milk.restfilelogger.service;

import com.milk.restfilelogger.entity.*;
import com.milk.restfilelogger.exception.EventNotFoundException;
import com.milk.restfilelogger.repository.EventRepository;
import com.milk.restfilelogger.service.implementation.EventServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Jack Milk
 */

@ExtendWith(MockitoExtension.class)
public class EventServiceImplTests {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    private UserEntity getUser(){
        UserEntity newUser = new UserEntity();
        newUser.setId(1L);
        newUser.setEmail("johndoe@yahoo.com");
        newUser.setName("JohnDoe");
        newUser.setRole(Role.ADMIN);
        newUser.setStatus(Status.ACTIVE);
        return newUser;
    }

    public List<FileEntity> getFiles() {
        return Stream.of(
                new FileEntity("test_file.txt", "/path/to/file"),
                new FileEntity("new_file.txt", "/path/to/file"),
                new FileEntity("some_file.txt", "/path/to/file")
        ).collect(Collectors.toList());
    }

    public EventEntity getEvent() {
        EventEntity event = new EventEntity();
        event.setId(1L);
        event.setUser(getUser());
        event.setFile(getFiles().get(0));
        event.setOccasion(Occasion.UPLOAD);
        event.setDate(LocalDateTime.now());
        return event;
    }

    public List<EventEntity> getEvents(){
        List<EventEntity> events = new ArrayList<>();
        events.add(new EventEntity(getUser(), getFiles().get(0), Occasion.UPLOAD, LocalDateTime.now()));
        events.add(new EventEntity(getUser(), getFiles().get(1), Occasion.DOWNLOAD, LocalDateTime.now()));
        events.add(new EventEntity(getUser(), getFiles().get(2), Occasion.RENAME, LocalDateTime.now()));
        return events;
    }

    public List<EventEntity> getFileEvents(){
        List<EventEntity> events = new ArrayList<>();
        events.add(new EventEntity(getUser(), getFiles().get(1), Occasion.UPLOAD, LocalDateTime.now()));
        events.add(new EventEntity(getUser(), getFiles().get(1), Occasion.DOWNLOAD, LocalDateTime.now()));
        events.add(new EventEntity(getUser(), getFiles().get(1), Occasion.RENAME, LocalDateTime.now()));
        return events;
    }



    @Test
    public void getAllEventsTest() {
        when(eventRepository.findAll()).thenReturn(getEvents());
        assertEquals(3, eventService.getAll().size());
        assertEquals("JohnDoe", eventService.getAll().get(0).getUser().getName());
        assertEquals("johndoe@yahoo.com", eventService.getAll().get(0).getUser().getEmail());
        assertEquals("ADMIN", eventService.getAll().get(0).getUser().getRole().name());
        assertEquals("ACTIVE", eventService.getAll().get(0).getUser().getStatus().name());

        assertEquals("test_file.txt", eventService.getAll().get(0).getFile().getName());
        assertEquals("/path/to/file", eventService.getAll().get(0).getFile().getPath());

        assertEquals("UPLOAD", eventService.getAll().get(0).getOccasion().name());

        verify(eventRepository, times(8)).findAll();
    }

    @Test
    public void getEventByIdTest() throws EventNotFoundException {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(getEvent()));
        assertEquals(1L, eventService.getEventById(1L).getId());
        assertEquals("JohnDoe", eventService.getEventById(1L).getUser().getName());
        assertEquals("johndoe@yahoo.com", eventService.getEventById(1L).getUser().getEmail());
        assertEquals("ADMIN", eventService.getEventById(1L).getUser().getRole().name());
        assertEquals("ACTIVE", eventService.getEventById(1L).getUser().getStatus().name());

        assertEquals("test_file.txt", eventService.getEventById(1L).getFile().getName());
        assertEquals("/path/to/file", eventService.getEventById(1L).getFile().getPath());

        assertEquals("UPLOAD", eventService.getEventById(1L).getOccasion().name());

        verify(eventRepository, times(8)).findById(1L);
    }

    @Test
    public void getEventsByUserIdTest() {
        when(eventRepository.getAllByUserId(1L)).thenReturn(getEvents());
        assertEquals(3, eventService.getEventsByUserId(1L).size());
        assertEquals("JohnDoe", eventService.getEventsByUserId(1L).get(0).getUser().getName());
        assertEquals("johndoe@yahoo.com", eventService.getEventsByUserId(1L).get(0).getUser().getEmail());

        assertEquals("new_file.txt", eventService.getEventsByUserId(1L).get(1).getFile().getName());
        assertEquals("/path/to/file", eventService.getEventsByUserId(1L).get(1).getFile().getPath());

        assertEquals("DOWNLOAD", eventService.getEventsByUserId(1L).get(1).getOccasion().name());

        verify(eventRepository, times(6)).getAllByUserId(1L);
    }

    @Test
    public void getEventsByFileIdTest() {
        when(eventRepository.getAllByFileId(2L)).thenReturn(getFileEvents());
        assertEquals(3, eventService.getEventsByFileId(2L).size());
        assertEquals("new_file.txt", eventService.getEventsByFileId(2L).get(0).getFile().getName());
        assertEquals("new_file.txt", eventService.getEventsByFileId(2L).get(1).getFile().getName());
        assertEquals("new_file.txt", eventService.getEventsByFileId(2L).get(2).getFile().getName());

        assertEquals("UPLOAD", eventService.getEventsByFileId(2L).get(0).getOccasion().name());
        assertEquals("DOWNLOAD", eventService.getEventsByFileId(2L).get(1).getOccasion().name());
        assertEquals("RENAME", eventService.getEventsByFileId(2L).get(2).getOccasion().name());

        verify(eventRepository, times(7)).getAllByFileId(2L);
    }

    @Test
    public void saveEventTest() {
        when(eventRepository.save(Mockito.any(EventEntity.class))).thenReturn(getEvent());
        assertEquals(1L, eventService.save(getEvent()).getId());
        assertEquals("JohnDoe", eventService.save(getEvent()).getUser().getName());
        assertEquals("johndoe@yahoo.com", eventService.save(getEvent()).getUser().getEmail());

        assertEquals("test_file.txt", eventService.save(getEvent()).getFile().getName());
        assertEquals("/path/to/file", eventService.save(getEvent()).getFile().getPath());

        assertEquals("UPLOAD", eventService.save(getEvent()).getOccasion().name());

        verify(eventRepository, times(6)).save(Mockito.any(EventEntity.class));
    }

    @Test
    public void deleteEventTest() {
        doNothing().when(eventRepository).deleteById(1L);
        eventService.delete(1L);
        verify(eventRepository).deleteById(1L);
    }

}

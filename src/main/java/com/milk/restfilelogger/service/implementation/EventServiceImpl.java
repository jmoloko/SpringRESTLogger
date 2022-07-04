package com.milk.restfilelogger.service.implementation;

import com.milk.restfilelogger.entity.EventEntity;
import com.milk.restfilelogger.exception.EventNotFoundException;
import com.milk.restfilelogger.repository.EventRepository;
import com.milk.restfilelogger.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jack Milk
 */

@RequiredArgsConstructor
@Service
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public List<EventEntity> getAll() {
        log.info("Fetching all events");
        return eventRepository.findAll();
    }

    @Override
    public EventEntity getEventById(Long id) throws EventNotFoundException {
        log.info("Fetching event by id: {}", id);
        return eventRepository.findById(id).orElseThrow(() -> {
            log.error("Event by id: {} not found", id);
            return new EventNotFoundException("Event NOT Found");
        });
    }

    @Override
    public List<EventEntity> getEventsByUserId(Long id) {
        log.info("Fetching event by user id: {}", id);
        return eventRepository.getAllByUserId(id);
    }

    @Override
    public List<EventEntity> getEventsByFileId(Long id) {
        log.info("Fetching event by file id: {}", id);
        return eventRepository.getAllByFileId(id);
    }

    @Override
    public EventEntity save(EventEntity event) {
        log.info("Save new event");
        return eventRepository.save(event);
    }

    @Override
    public void delete(Long id) {
        log.info("Delete event by id: {}", id);
        eventRepository.deleteById(id);
    }
}

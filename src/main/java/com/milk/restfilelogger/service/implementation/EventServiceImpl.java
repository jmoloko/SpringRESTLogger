package com.milk.restfilelogger.service.implementation;

import com.milk.restfilelogger.entity.EventEntity;
import com.milk.restfilelogger.exception.EventNotFoundException;
import com.milk.restfilelogger.repository.EventRepository;
import com.milk.restfilelogger.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jack Milk
 */
@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<EventEntity> getAll() {
        return eventRepository.findAll();
    }

    @Override
    public EventEntity getEventById(Long id) throws EventNotFoundException {
        return eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("Event NOT Found"));
    }

    @Override
    public List<EventEntity> getEventsByUserId(Long id) {
        return eventRepository.getAllByUserId(id);
    }

    @Override
    public List<EventEntity> getEventsByFileId(Long id) {
        return eventRepository.getAllByFileId(id);
    }

    @Override
    public EventEntity save(EventEntity event) {
        return eventRepository.save(event);
    }

    @Override
    public void delete(Long id) {
        eventRepository.deleteById(id);
    }
}

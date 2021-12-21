package com.milk.restfilelogger.service.implementation;

import com.milk.restfilelogger.dto.EventDTO;
import com.milk.restfilelogger.entity.EventEntity;
import com.milk.restfilelogger.exception.EventNotFoundException;
import com.milk.restfilelogger.repository.EventRepository;
import com.milk.restfilelogger.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jack Milk
 */
@Service
public class EventServiceImpl implements EventService {

    @Autowired
    EventRepository eventRepository;

    @Override
    public List<EventDTO> getAll() {
        return eventRepository.findAll().stream().map(EventDTO::toDto).collect(Collectors.toList());
    }

    @Override
    public EventDTO getEventById(Long id) throws EventNotFoundException {
        return EventDTO.toDto(eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("Event NOT Found")));
    }

    @Override
    public List<EventDTO> getEventsByUserId(Long id) {
        return eventRepository.getAllByUserId(id).stream().map(EventDTO::toDto).collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> getEventsByFileId(Long id) {
        return eventRepository.getAllByFileId(id).stream().map(EventDTO::toDto).collect(Collectors.toList());
    }

    @Override
    public EventDTO save(EventEntity event) {
        return EventDTO.toDto(eventRepository.save(event));
    }

    @Override
    public EventDTO update(EventEntity event, Long id) throws EventNotFoundException {
        EventEntity uEvent = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("Event NOT Found"));
        uEvent.setUser(event.getUser());
        uEvent.setFile(event.getFile());
        uEvent.setDate(event.getDate());
        uEvent.setOccasion(event.getOccasion());
        return EventDTO.toDto(eventRepository.save(uEvent));
    }

    @Override
    public Long delete(Long id) {
        eventRepository.deleteById(id);
        return id;
    }
}

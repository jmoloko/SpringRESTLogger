package com.milk.restfilelogger.service;

import com.milk.restfilelogger.dto.EventDTO;
import com.milk.restfilelogger.entity.EventEntity;
import com.milk.restfilelogger.exception.EventNotFoundException;

import java.util.List;

/**
 * @author Jack Milk
 */
public interface EventService {
    List<EventDTO> getAll();
    EventDTO getEventById(Long id) throws EventNotFoundException;
    List<EventDTO> getEventsByUserId(Long id);
    List<EventDTO> getEventsByFileId(Long id);
    EventDTO save(EventEntity event);
    EventDTO update(EventEntity event, Long id) throws EventNotFoundException;
    Long delete(Long id);
}

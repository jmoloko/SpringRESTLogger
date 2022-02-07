package com.milk.restfilelogger.service;

import com.milk.restfilelogger.entity.EventEntity;
import com.milk.restfilelogger.exception.EventNotFoundException;

import java.util.List;

/**
 * @author Jack Milk
 */
public interface EventService {
    List<EventEntity> getAll();
    EventEntity getEventById(Long id) throws EventNotFoundException;
    List<EventEntity> getEventsByUserId(Long id);
    List<EventEntity> getEventsByFileId(Long id);
    EventEntity save(EventEntity event);
    void delete(Long id);
}

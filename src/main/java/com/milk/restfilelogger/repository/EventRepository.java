package com.milk.restfilelogger.repository;

import com.milk.restfilelogger.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Jack Milk
 */
public interface EventRepository extends JpaRepository<EventEntity, Long> {
    List<EventEntity> getAllByUserId(Long id);
    List<EventEntity> findAllByUserId(Long id);
    List<EventEntity> getAllByFileId(Long id);
    List<EventEntity> findAllByFileId(Long id);
}

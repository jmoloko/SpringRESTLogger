package com.milk.restfilelogger.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.milk.restfilelogger.entity.EventEntity;
import com.milk.restfilelogger.entity.Occasion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author Jack Milk
 */
@Getter
@Setter
@NoArgsConstructor
public class EventDTO {
    private Long id;
    private FileDTO file;
    private Occasion occasion;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime date;

    public static EventDTO toDto(EventEntity entity){
        EventDTO eventDto = new EventDTO();
        eventDto.setId(entity.getId());
        eventDto.setFile(FileDTO.toDto(entity.getFile()));
        eventDto.setOccasion(entity.getOccasion());
        eventDto.setDate(entity.getDate());
        return eventDto;
    }
}

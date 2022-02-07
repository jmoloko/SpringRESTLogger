package com.milk.restfilelogger.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
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
    @JsonView(EventViews.ShortView.class)
    private Long id;
    @JsonView(EventViews.ShortView.class)
    private FileDTO file;
    @JsonView(EventViews.ShortView.class)
    private Occasion occasion;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonView(EventViews.ShortView.class)
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

package com.milk.restfilelogger.dto;

import com.milk.restfilelogger.entity.Role;
import com.milk.restfilelogger.entity.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jack Milk
 */
@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private Role role;
    List<EventDTO> events;

    public static UserDTO toDto(UserEntity entity){
        UserDTO userDto = new UserDTO();
        userDto.setId(entity.getId());
        userDto.setName(entity.getName());
        userDto.setRole(entity.getRole());
        userDto.setEvents(entity.getEvents().stream().map(EventDTO::toDto).collect(Collectors.toList()));
        return userDto;
    }
}

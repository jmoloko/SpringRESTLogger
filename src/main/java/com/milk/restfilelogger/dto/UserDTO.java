package com.milk.restfilelogger.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.milk.restfilelogger.entity.Role;
import com.milk.restfilelogger.entity.Status;
import com.milk.restfilelogger.entity.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Jack Milk
 */
@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    @JsonView({JsonViews.ShortView.class})
    private Long id;
    @JsonView({JsonViews.ShortView.class})
    private String email;
    @JsonView({JsonViews.ShortView.class})
    private String name;
    @JsonView({JsonViews.FullView.class})
    private Role role;
    @JsonView({JsonViews.FullView.class})
    private Status status;


    public static UserDTO toDto(UserEntity entity){
        UserDTO userDto = new UserDTO();
        userDto.setId(entity.getId());
        userDto.setEmail(entity.getEmail());
        userDto.setName(entity.getName());
        userDto.setRole(entity.getRole());
        userDto.setStatus(entity.getStatus());
        return userDto;
    }
}

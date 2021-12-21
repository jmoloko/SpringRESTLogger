package com.milk.restfilelogger.dto;

import com.milk.restfilelogger.entity.FileEntity;
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
public class FileDTO {
    private Long id;
    private String name;
    private String path;

    public static FileDTO toDto(FileEntity entity){
        FileDTO fileDto = new FileDTO();
        fileDto.setId(entity.getId());
        fileDto.setName(entity.getName());
        fileDto.setPath(entity.getPath());
        return fileDto;
    }
}

package com.milk.restfilelogger.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * @author Jack Milk
 */
@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
public class FileEntity extends BaseEntity{
    private String name;
    private String path;

    @OneToMany(mappedBy = "file")
    private List<EventEntity> events;
}

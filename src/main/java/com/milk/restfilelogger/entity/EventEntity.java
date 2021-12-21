package com.milk.restfilelogger.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Jack Milk
 */
@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
public class EventEntity extends BaseEntity{

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "file_id")
    private FileEntity file;

    @Enumerated(EnumType.STRING)
    private Occasion occasion;

    private LocalDateTime date;
}

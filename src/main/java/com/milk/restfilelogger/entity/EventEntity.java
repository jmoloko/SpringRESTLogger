package com.milk.restfilelogger.entity;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class EventEntity extends BaseEntity{

    public EventEntity(UserEntity user, FileEntity file, Occasion occasion) {
        this.user = user;
        this.file = file;
        this.occasion = occasion;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "file_id")
    private FileEntity file;

    @Enumerated(EnumType.STRING)
    @Column(name = "occasion")
    private Occasion occasion;

    @Column(name = "date")
    private LocalDateTime date;
}

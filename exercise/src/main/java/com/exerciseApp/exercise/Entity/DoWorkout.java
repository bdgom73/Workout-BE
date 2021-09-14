package com.exerciseApp.exercise.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class DoWorkout extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "do_workout_id")
    private Long id;

    private Boolean isWorkout;
    private LocalDate date;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}

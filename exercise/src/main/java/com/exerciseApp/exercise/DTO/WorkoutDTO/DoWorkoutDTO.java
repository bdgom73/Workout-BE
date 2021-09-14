package com.exerciseApp.exercise.DTO.WorkoutDTO;

import com.exerciseApp.exercise.Entity.BaseEntity;
import com.exerciseApp.exercise.Entity.DoWorkout;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DoWorkoutDTO extends BaseEntity {

    private Long id;
    private Boolean isWorkout;
    private LocalDate start;
    private String memo;
    private List<RoutineSmallDTO> routines = new ArrayList<>();

    public DoWorkoutDTO(DoWorkout doWorkout) {
        this.id = doWorkout.getId();
        this.isWorkout = doWorkout.getIsWorkout();
        this.start = doWorkout.getDate();
    }
}

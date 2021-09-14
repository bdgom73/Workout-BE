package com.exerciseApp.exercise.DTO.WorkoutDTO;

import com.exerciseApp.exercise.Entity.Routine;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoutineSmallDTO {
    private Long id;
    private String title;
    private String part;

    public RoutineSmallDTO(Routine routine) {
        this.id = routine.getId();
        this.title = routine.getTitle();
        this.part = routine.getPart();
    }

}

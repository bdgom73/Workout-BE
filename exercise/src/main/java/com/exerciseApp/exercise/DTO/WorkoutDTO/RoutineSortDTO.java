package com.exerciseApp.exercise.DTO.WorkoutDTO;

import com.exerciseApp.exercise.Entity.Routine;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoutineSortDTO {


    private Routine routine;

    private Integer score;

    public RoutineSortDTO(Routine routine, Integer score) {
        this.routine = routine;
        this.score = score;
    }
}

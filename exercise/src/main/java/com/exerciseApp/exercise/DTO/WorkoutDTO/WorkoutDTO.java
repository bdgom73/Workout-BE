package com.exerciseApp.exercise.DTO.WorkoutDTO;

import com.exerciseApp.exercise.Entity.Workout;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class WorkoutDTO {

    private Long id;
    private String name;
    private String workout_img;
    private String part;
    private String target;
    private int e_type;

    public WorkoutDTO(Workout workout) {
        this.id = workout.getId();
        this.name = workout.getName();
        this.workout_img = workout.getWorkout_img();
        this.part = workout.getPart();
        this.target = workout.getTarget();
        this.e_type = workout.getE_type();
    }
}

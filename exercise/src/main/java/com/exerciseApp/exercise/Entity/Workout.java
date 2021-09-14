package com.exerciseApp.exercise.Entity;

import com.exerciseApp.exercise.DTO.WorkoutDTO.WorkoutRegister;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Workout extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workout_id")
    private Long id;
    private String name;
    private String workout_img;
    private String part;
    private Integer e_type;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    public Workout(WorkoutRegister workoutRegister) {
        this.name = workoutRegister.getName();
        this.part = workoutRegister.getPart();
        this.e_type = workoutRegister.getE_type();
        this.explanation = workoutRegister.getExplanation();
    }
}

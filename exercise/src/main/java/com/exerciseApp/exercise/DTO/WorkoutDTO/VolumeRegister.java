package com.exerciseApp.exercise.DTO.WorkoutDTO;

import com.exerciseApp.exercise.Entity.Routine;
import com.exerciseApp.exercise.Entity.Workout;
import lombok.*;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class VolumeRegister {

    private int num;
    private int sets;
    private int orders;

    private Long workout_id;
}

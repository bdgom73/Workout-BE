package com.exerciseApp.exercise.DTO.WorkoutDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VolumeRegister {

    private Long id;
    private Integer num;
    private Integer sets;

    private Long workout_id;
}

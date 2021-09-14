package com.exerciseApp.exercise.DTO.WorkoutDTO;

import com.exerciseApp.exercise.Entity.Volume;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VolumeDTO {

    private Long id;
    private int num;
    private int sets;

    private WorkoutDTO workout;


    public VolumeDTO(Volume volume) {
        this.id = volume.getId();
        this.num = volume.getNum();
        this.sets = volume.getSets();
        this.workout = new WorkoutDTO(volume.getWorkout());
    }

}

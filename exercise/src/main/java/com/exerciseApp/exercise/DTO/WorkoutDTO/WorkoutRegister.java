package com.exerciseApp.exercise.DTO.WorkoutDTO;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder @Getter @Setter
public class WorkoutRegister {

    private String name;

    private MultipartFile workout_imageFile;

    private String part;

    private String target;

    private int e_type;
}

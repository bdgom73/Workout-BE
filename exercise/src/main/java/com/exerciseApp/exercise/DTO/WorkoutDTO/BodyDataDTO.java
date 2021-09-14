package com.exerciseApp.exercise.DTO.WorkoutDTO;

import com.exerciseApp.exercise.Entity.BodyData;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class BodyDataDTO {

    private Long id;

    private Integer age;
    private Double height;
    private Double weight;


    // 골격근량
    private Double SMM;


    public BodyDataDTO(BodyData bodyData) {
        this.id = bodyData.getId();
        this.age = bodyData.getAge();
        this.height = bodyData.getHeight();
        this.weight = bodyData.getWeight();
        this.SMM = bodyData.getSMM();
    }
}

package com.exerciseApp.exercise.DTO.MemberDTO;

import com.exerciseApp.exercise.Entity.WeightLog;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class WeightLogDTO {

    private Long id;

    private LocalDateTime date;
    private Double weight;

    private MemberDTO member;

    public WeightLogDTO(WeightLog weightLog) {
        this.id = weightLog.getId();
        this.date = weightLog.getDate();
        this.weight = weightLog.getWeight();
        this.member = new MemberDTO(weightLog.getMember());
    }
}

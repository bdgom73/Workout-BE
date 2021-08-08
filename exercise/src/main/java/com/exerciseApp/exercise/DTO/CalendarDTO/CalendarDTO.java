package com.exerciseApp.exercise.DTO.CalendarDTO;

import com.exerciseApp.exercise.DTO.MemberDTO.MemberDTO;
import com.exerciseApp.exercise.Entity.Calendar;
import com.exerciseApp.exercise.Entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter @Setter
public class CalendarDTO {

    private Long id;

    private LocalDate currentDate;

    private boolean isWorkout;

    private String memo;

    private MemberDTO member;

    public CalendarDTO(Calendar calendar) {
        this.id = calendar.getId();
        this.currentDate = calendar.getCurrentDate();
        this.isWorkout = calendar.isWorkout();
        this.memo = calendar.getMemo();
        this.member = new MemberDTO(calendar.getMember());
    }

    public CalendarDTO(){

    }
}

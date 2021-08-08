package com.exerciseApp.exercise.Entity;

import com.exerciseApp.exercise.DTO.CalendarDTO.CalendarRegister;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter @Setter
public class Calendar{

    @Id @GeneratedValue
    @Column(name = "calendar_id")
    private Long id;

    @Column(name = "current_date")
    private LocalDate currentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private boolean isWorkout;

    @Column(columnDefinition = "text")
    private String memo;

    // 생성자
    public Calendar(String date ,Member member, boolean isWorkout){
        this.currentDate = LocalDate.parse(date);
        this.member = member;
        this.isWorkout = isWorkout;
    }

    public Calendar(CalendarRegister calendarRegister,Member member) {
        this.currentDate = LocalDate.parse(calendarRegister.getDate());
        this.isWorkout = calendarRegister.isWorkout();
        this.member = member;
    }


}

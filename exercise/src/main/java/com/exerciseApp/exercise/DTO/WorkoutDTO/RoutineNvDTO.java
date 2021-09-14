package com.exerciseApp.exercise.DTO.WorkoutDTO;

import com.exerciseApp.exercise.Entity.Routine;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RoutineNvDTO {

    private Long id;
    private String title;
    private String part;

    private Boolean share;

    private Long member_id;
    private String member_email;
    private String member_name;

    private Integer recommend;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public RoutineNvDTO(Routine routine) {
        this.id = routine.getId();
        this.title = routine.getTitle();
        this.part = routine.getPart();
        this.share = routine.getShare();
        this.member_id = routine.getMember().getId();
        this.member_email = routine.getMember().getEmail();
        this.member_name = routine.getMember().getNickname();
        this.createdDate = routine.getCreatedDate();
        this.modifiedDate = routine.getModifiedDate();
        this.recommend = routine.getRecommends().size();
    }

    public RoutineNvDTO(RoutineSortDTO routine) {
        this.id = routine.getRoutine().getId();
        this.title = routine.getRoutine().getTitle();
        this.part = routine.getRoutine().getPart();
        this.share = routine.getRoutine().getShare();
        this.member_id = routine.getRoutine().getMember().getId();
        this.member_email = routine.getRoutine().getMember().getEmail();
        this.member_name = routine.getRoutine().getMember().getNickname();
        this.createdDate = routine.getRoutine().getCreatedDate();
        this.modifiedDate = routine.getRoutine().getModifiedDate();
        this.recommend = routine.getRoutine().getRecommends().size();
    }
}

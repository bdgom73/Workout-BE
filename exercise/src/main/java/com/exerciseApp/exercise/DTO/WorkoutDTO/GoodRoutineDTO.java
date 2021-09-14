package com.exerciseApp.exercise.DTO.WorkoutDTO;

import com.exerciseApp.exercise.Entity.Routine;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class GoodRoutineDTO implements Comparable<GoodRoutineDTO> {

    private Long id;
    private String title;
    private String part;

    private Long member_id;
    private String member_email;
    private String member_name;

    private int recommend;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public GoodRoutineDTO(RecommendRoutineDTO recommendRoutineDTO) {
        this.id = recommendRoutineDTO.getRoutine().getId();
        this.title = recommendRoutineDTO.getRoutine().getTitle();
        this.part = recommendRoutineDTO.getRoutine().getPart();
        this.member_id = recommendRoutineDTO.getRoutine().getMember().getId();
        this.member_email = recommendRoutineDTO.getRoutine().getMember().getEmail();
        this.member_name = recommendRoutineDTO.getRoutine().getMember().getNickname();
        this.recommend = recommendRoutineDTO.getCnt();
        this.createdDate = recommendRoutineDTO.getRoutine().getCreatedDate();
        this.modifiedDate = recommendRoutineDTO.getRoutine().getModifiedDate();
    }

    public GoodRoutineDTO(Routine routine) {
        this.id = routine.getId();
        this.title = routine.getTitle();
        this.part = routine.getPart();
        this.member_id = routine.getMember().getId();
        this.member_email = routine.getMember().getEmail();
        this.member_name = routine.getMember().getNickname();
        this.recommend = routine.getRecommends().size();
        this.createdDate = routine.getCreatedDate();
        this.modifiedDate = routine.getModifiedDate();
    }

    public GoodRoutineDTO(Routine routine, Long count) {
        this.id = routine.getId();
        this.title = routine.getTitle();
        this.part = routine.getPart();
        this.member_id = routine.getMember().getId();
        this.member_email = routine.getMember().getEmail();
        this.member_name = routine.getMember().getNickname();
        this.recommend = Math.toIntExact(count);
        this.createdDate = routine.getCreatedDate();
        this.modifiedDate = routine.getModifiedDate();
    }

    @Override
    public int compareTo(GoodRoutineDTO goodRoutineDTO) {
        return this.getCreatedDate().compareTo(goodRoutineDTO.getCreatedDate());
    }
}

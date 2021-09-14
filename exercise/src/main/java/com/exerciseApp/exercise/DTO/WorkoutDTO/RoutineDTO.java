package com.exerciseApp.exercise.DTO.WorkoutDTO;

import com.exerciseApp.exercise.Entity.Member;
import com.exerciseApp.exercise.Entity.Routine;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class RoutineDTO {

    private Long id;
    private String title;
    private String part;

    private Boolean share;

    private Long member_id;
    private String member_email;
    private String member_name;

    private Long originalAuthor;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    private List<VolumeDTO> volumes = new ArrayList<>();

    public RoutineDTO(Routine routine) {
        this.id = routine.getId();
        this.title = routine.getTitle();
        this.part = routine.getPart();
        this.member_id = routine.getMember().getId();
        this.member_email = routine.getMember().getEmail();
        this.member_name = routine.getMember().getNickname();
        this.volumes = routine.getVolumes().stream().map(VolumeDTO::new).collect(Collectors.toList());
        this.share = routine.getShare();
        this.createdDate = routine.getCreatedDate();
        this.modifiedDate = routine.getModifiedDate();
        this.originalAuthor = routine.getOriginalAuthor();
    }

    public RoutineDTO(Routine routine, Member member) {
        this.id = routine.getId();
        this.title = routine.getTitle();
        this.part = routine.getPart();
        this.member_id = member.getId();
        this.member_email = member.getEmail();
        this.member_name = member.getNickname();
        this.volumes = routine.getVolumes().stream().map(VolumeDTO::new).collect(Collectors.toList());
        this.createdDate = routine.getCreatedDate();
        this.modifiedDate = routine.getModifiedDate();
        this.originalAuthor = routine.getOriginalAuthor();
        this.share = Boolean.FALSE;
    }

}

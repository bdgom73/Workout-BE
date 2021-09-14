package com.exerciseApp.exercise.DTO.WorkoutDTO;

import com.exerciseApp.exercise.Entity.Routine;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ShareRoutineDTO {

    private Long id;
    private String title;
    private String part;

    private Long member_id;
    private String member_email;
    private String member_name;

    private Long recommend;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    private List<VolumeDTO> volumes;

    public ShareRoutineDTO(Routine routine, Long count) {
        this.id = routine.getId();
        this.title = routine.getTitle();
        this.part = routine.getPart();
        this.member_id = routine.getMember().getId();
        this.member_email = routine.getMember().getEmail();
        this.member_name = routine.getMember().getNickname();
        this.recommend = count;
        this.createdDate = routine.getCreatedDate();
        this.modifiedDate = routine.getModifiedDate();
        this.volumes = routine.getVolumes().stream().map(VolumeDTO::new).collect(Collectors.toList());
    }
}

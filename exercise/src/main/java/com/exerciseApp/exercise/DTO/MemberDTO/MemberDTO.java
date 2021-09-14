package com.exerciseApp.exercise.DTO.MemberDTO;

import com.exerciseApp.exercise.Entity.Member;
import com.exerciseApp.exercise.Enum.MemberRank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberDTO {

    private Long id;
    private String email;
    private String name;
    private String avatar_url;
    private MemberRank rank;

    public MemberDTO(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.name = member.getName();
        this.avatar_url = member.getAvatar_url();
        this.rank = member.getMemberRank();
    }
}

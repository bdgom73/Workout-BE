package com.exerciseApp.exercise.Entity;

import com.exerciseApp.exercise.Enum.MemberRank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
public class Member extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String email;
    private String password;
    private String name;
    private String nickname;

    @Column(name = "member_rank")
    @Enumerated(EnumType.STRING)
    private MemberRank memberRank;

    private String avatar_url;
    private String SESSID;

    public Member() {this(null,null,null,null,null);}

    public Member(String email, String password, String name, String nickname,String SESSID) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.memberRank = MemberRank.USER;
        this.SESSID = SESSID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMember_rank(MemberRank member_rank) {
        this.memberRank = member_rank;
    }

    public void setSESSID(String SESSID) {
        this.SESSID = SESSID;
    }

    public void setAvatarUrl(String avatar_url) {
        this.avatar_url = avatar_url;
    }
}

package com.exerciseApp.exercise.Entity;

import com.exerciseApp.exercise.Enum.LoginType;
import com.exerciseApp.exercise.Enum.MemberRank;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue
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

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type")
    private LoginType type;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private BodyData bodyData;

    public Member() {
        this(null, null, null, null, null);
    }

    public Member(String email, String password, String name, String nickname, String SESSID) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.memberRank = MemberRank.USER;
        this.SESSID = SESSID;
    }


}

package com.exerciseApp.exercise.DTO.MemberDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@NoArgsConstructor
public class Register {

    private String email;
    private String password1;
    private String password2;
    private String name;
    private String nickname;

    public Register(String email, String password1, String password2, String name, String nickname) {
        this.email = email;
        this.password1 = password1;
        this.password2 = password2;
        this.name = name;
        this.nickname = nickname;
    }
}

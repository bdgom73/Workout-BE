package com.exerciseApp.exercise.DTO.MemberDTO;

import lombok.*;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;


@Getter @Setter
@Builder
@NoArgsConstructor
public class Register {

    private String email;
    private String password1;
    private String password2;
    private String name;
    private String nickname;
    private MultipartFile avatar;

    public Register(String email, String password1, String password2, String name, String nickname,MultipartFile avatar) {
        this.email = email;
        this.password1 = password1;
        this.password2 = password2;
        this.name = name;
        this.nickname = nickname;
        this.avatar = avatar;
    }
}

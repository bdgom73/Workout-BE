package com.exerciseApp.exercise.Service;

import com.exerciseApp.exercise.DTO.MemberDTO.MemberDTO;
import com.exerciseApp.exercise.DTO.MemberDTO.Register;
import com.exerciseApp.exercise.DTO.ResData;
import com.exerciseApp.exercise.Entity.Member;
import com.exerciseApp.exercise.Repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JWTService jwtService;
    private final CreateIDService createIDService;
    private final FileUpload fileUpload;

    public MemberService(MemberRepository memberRepository, JWTService jwtService, CreateIDService createIDService, FileUpload fileUpload) {
        this.memberRepository = memberRepository;
        this.jwtService = jwtService;
        this.createIDService = createIDService;
        this.fileUpload = fileUpload;
    }

    /* public */

    public ResData Login(String email, String password) {
        // TODO LOGIN Service
        Optional<Member> findMember = memberRepository.findByEmail(email);
        if (findMember.isEmpty()) {
            return ResData.builder()
                    .message("존재하지 않는 사용자입니다.")
                    .data(null)
                    .result_state(false).build();
        }
        Member member = findMember.get();
        if (!createIDService.matchPassword(password, member.getPassword())) {
            return ResData.builder()
                    .message("비밀번호가 틀립니다.")
                    .data(null)
                    .result_state(false).build();
        }
        return ResData.builder()
                .message("성공적으로 로그인 하셨습니다.")
                .data(jwtService.createToken(member.getSESSID()))
                .result_state(true).build();
    }

    public ResData Login(String email, String password, Boolean keep) {
        // TODO LOGIN Service
        Optional<Member> findMember = memberRepository.findByEmail(email);
        if (findMember.isEmpty()) {
            return ResData.builder()
                    .message("존재하지 않는 사용자입니다.")
                    .data(null)
                    .result_state(false).build();
        }
        Member member = findMember.get();
        if (!createIDService.matchPassword(password, member.getPassword())) {
            return ResData.builder()
                    .message("비밀번호가 틀립니다.")
                    .data(null)
                    .result_state(false).build();
        }
        if (member.getPassword() == null) {
            return ResData.builder()
                    .message("비밀번호가 틀립니다.")
                    .data(null)
                    .result_state(false).build();
        }

        return ResData.builder()
                .message("성공적으로 로그인 하셨습니다.")
                .data(keep ? jwtService.createTokenNotExp(member.getSESSID()) : jwtService.createToken(member.getSESSID()))
                .result_state(true).build();
    }

    public ResData SignUp(Register register) {
        // TODO SignUp Service
        Optional<Member> findMember = memberRepository.findByEmail(register.getEmail());
        if (findMember.isPresent()) {
            return ResData.builder()
                    .message("이미 가입된 이메일입니다.").data(null).result_state(false).build();
        }
        if (!register.getPassword1().equals(register.getPassword2())) {
            return ResData.builder()
                    .message("비밀번호가 서로 다릅니다.").data(null).result_state(false).build();
        }
        if (!passwordRegex(register.getPassword1())) {
            return ResData.builder()
                    .message("비밀번호의 형식이 올바르지 않습니다.").data(null).result_state(false).build();
        }
        String private_key = UUID.randomUUID().toString();
        String pw = createIDService.encodePassword(register.getPassword1());
        Member member = new Member(register.getEmail(), pw, register.getName(), register.getNickname(), private_key);
        memberRepository.save(member);

        return ResData.builder()
                .message("성공적으로 회원 등록이 되었습니다").data("success").result_state(true).build();
    }

    public ResData AuthenticationMemberInformation(String SSID) {
        // TODO Authenticated Member Information 로그인된 멤버의 정보 리턴
        Optional<Member> findSSID = memberRepository.findBySESSID(SSID);
        if (findSSID.isEmpty()) {
            return ResData.builder().message("Log off").data(null).result_state(false).build();
        }
        Member member = findSSID.get();
        return ResData.builder().message("Sign in").data(new MemberDTO(member)).result_state(true).build();
    }

    public Register toRegisterObject(
            String email, String password1, String password2, String name, String nickname, MultipartFile avatar
    ) {
        return Register.builder().email(email).password1(password1).password2(password2).nickname(nickname).name(name)
                .build();
    }

    /* private */

    private boolean passwordRegex(String password) {
        // TODO password Regex 암호의 정규표현식
        String pattern = "^[a-zA-Z0-9\\d~!@#$%^&*]{10,}$";
        return Pattern.matches(pattern, password);

    }


}

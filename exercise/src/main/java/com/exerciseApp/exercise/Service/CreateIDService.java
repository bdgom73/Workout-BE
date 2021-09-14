package com.exerciseApp.exercise.Service;

import com.exerciseApp.exercise.Entity.Member;
import com.exerciseApp.exercise.Repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class CreateIDService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateIDService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String issueKey() {
        boolean isDup = Boolean.TRUE;
        String randomPID = createRandomPID();
        while (isDup) {
            Optional<Member> findSSID = memberRepository.findBySESSID(randomPID);
            if (findSSID.isPresent()) {
                randomPID = createRandomPID();
            } else {
                isDup = Boolean.FALSE;
            }
        }
        return randomPID;
    }

    public boolean matchPassword(String input_password, String saved_password) {
        return passwordEncoder.matches(input_password, saved_password);
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public String CreateRandomString(int len) {
        Random random = new Random();
        return random.ints(48, 122 + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(len)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private String createRandomPID() {
        StringBuilder sb = new StringBuilder();
        sb.append("Exc97");
        Random random = new Random();
        String rs = random.ints(48, 122 + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(50)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        sb.append(rs);
        return sb.toString();
    }


}

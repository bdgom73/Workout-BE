package com.exerciseApp.exercise.Controller;

import com.exerciseApp.exercise.DTO.MemberDTO.MemberDTO;
import com.exerciseApp.exercise.DTO.MemberDTO.Register;
import com.exerciseApp.exercise.DTO.MemberDTO.WeightLogDTO;
import com.exerciseApp.exercise.DTO.ResData;
import com.exerciseApp.exercise.DTO.WorkoutDTO.BodyDataDTO;
import com.exerciseApp.exercise.Entity.BodyData;
import com.exerciseApp.exercise.Entity.Member;
import com.exerciseApp.exercise.Entity.WeightLog;
import com.exerciseApp.exercise.Enum.LoginType;
import com.exerciseApp.exercise.Enum.MemberRank;
import com.exerciseApp.exercise.Repository.BodyDataRepository;
import com.exerciseApp.exercise.Repository.MemberRepository;
import com.exerciseApp.exercise.Repository.WeightLogRepository;
import com.exerciseApp.exercise.Service.FileUpload;
import com.exerciseApp.exercise.Service.JWTService;
import com.exerciseApp.exercise.Service.MemberService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/myApi/member")
public class MemberController {

    /* ex
     *  @PathVariable(test) String test "/test/{test}
     *  @PathParam(test) String test "/test?test=test
     * */
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final JWTService jwtService;
    private final FileUpload fileUpload;
    private final BodyDataRepository bodyDataRepository;
    private final WeightLogRepository weightLogRepository;

    private final String AUTH = "Authorization";

    public MemberController(MemberService memberService, MemberRepository memberRepository, JWTService jwtService, FileUpload fileUpload, BodyDataRepository bodyDataRepository, WeightLogRepository weightLogRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.jwtService = jwtService;
        this.fileUpload = fileUpload;
        this.bodyDataRepository = bodyDataRepository;
        this.weightLogRepository = weightLogRepository;
    }

    // 로그인
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResData Login(
            @RequestParam("email") String email, @RequestParam("password") String password,
            @PathParam("keep") boolean keep
    ) {
        return memberService.Login(email, password, keep);
    }

    @PostMapping(value = "/social/login")
    public ResData socialLogin(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "socialId") String socialId,
            @RequestParam(value = "image_url") String imageUrl
    ) {
        LoginType m_type = LoginType.EMAIL;
        if (type != null) {
            m_type = LoginType.valueOf(type.toUpperCase());
        }
        Optional<Member> findEmail = memberRepository.findByEmail(email);
        if (findEmail.isEmpty()) {
            Member member = new Member();
            member.setEmail(email);
            member.setMemberRank(MemberRank.USER);
            member.setName(name);
            member.setAvatar_url(imageUrl);
            member.setType(m_type);
            member.setSESSID(socialId);
            member.setNickname(name);
            memberRepository.save(member);
            return ResData.builder()
                    .message("성공적으로 로그인 하셨습니다.")
                    .data(jwtService.createToken(member.getSESSID()))
                    .result_state(true).build();
        }
        Member member = findEmail.get();
        if (findEmail.get().getType() != null) {
            if (findEmail.get().getType().equals(LoginType.GOOGLE) || findEmail.get().getType().equals(LoginType.GITHUB)) {
                member.setName(name);
                member.setNickname(name);
                member.setAvatar_url(imageUrl);
                return ResData.builder()
                        .message("성공적으로 로그인 하셨습니다.")
                        .data(jwtService.createToken(member.getSESSID()))
                        .result_state(true).build();
            }
        }

        //TODO social LOGIN
        return ResData.builder()
                .message("소셜로그인에 실패했습니다.")
                .data(null)
                .result_state(false).build();
    }

    /* 회원가입
     *   @Register JSON
     * */
    @PostMapping(value = "/signup")
    public ResData SignUp(@RequestBody Register register) {
        return memberService.SignUp(register);
    }

    // 로그인 정보
    @RequestMapping(value = "/authentication", method = RequestMethod.GET)
    public ResData AuthenticationMemberInformation(@RequestHeader(AUTH) String token) {
        String SSID = jwtService.getMemberSSID(token);
        String memberSSID = jwtService.getMemberSSID(SSID);
        return memberService.AuthenticationMemberInformation(memberSSID);
    }

    // (단일)유저 찾기
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public ResData findUser(
            @PathParam("type") String type,
            @PathParam("term") String term
    ) {
        if (type == null) type = "id";
        if (type.equals("nickname")) {
            Optional<Member> findNickname = memberRepository.findByNickname(term);
            if (findNickname.isEmpty())
                return ResData.builder().message("존재하지 않는 유저입니다").data(null).result_state(false).build();
            return ResData.builder().message("유저 검색에 성공했습니다").data(new MemberDTO(findNickname.get())).result_state(true).build();
        } else {
            Optional<Member> findID = memberRepository.findById(Long.parseLong(term));
            if (findID.isEmpty())
                return ResData.builder().message("존재하지 않는 유저입니다").data(null).result_state(false).build();
            return ResData.builder().message("유저 검색에 성공했습니다").data(new MemberDTO(findID.get())).result_state(true).build();
        }
    }

    // (복수)유저 찾기
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public ResData findAll(
            @PathParam("condition") String condition,
            @PathParam("term") String term,
            @PathParam("size") Integer size,
            @PathParam("limit") Integer page
    ) {
        StringBuilder sb;
        if (size == null) size = 10;
        if (page == null) page = 0;
        PageRequest pageRequest = PageRequest.of(page, size);
        if (condition != null) {
            if (term == null)
                return ResData.builder().message("조건(condition)을 선택하려면 조건값(term)값을 입력해주세요").data(new ArrayList<>()).result_state(false).build();
            switch (condition) {
                case "rank":
                    sb = new StringBuilder();
                    String msg = sb.append("page : ").append(page)
                            .append(", size :").append(size)
                            .append(", condition :").append(condition)
                            .append(", term : ").append(term).toString();
                    MemberRank memberRank = MemberRank.valueOf(term.toUpperCase());
                    Slice<Member> memberRankList = memberRepository.findSliceByMemberRank(memberRank, pageRequest);
                    return ResData.builder().message(msg).data(memberRankList.map(MemberDTO::new)).result_state(true).build();
                default:
                    return ResData.builder().message("해당 조건(condition)이 없습니다.").data(new ArrayList<>()).result_state(false).build();
            }
        }
        sb = new StringBuilder();
        String msg = sb.append("page : ").append(page).append(", size : ").append(size).toString();
        Slice<Member> memberRankList = memberRepository.findAll(pageRequest);
        return ResData.builder().message(msg).data(memberRankList.map(MemberDTO::new)).result_state(true).build();
    }

    @RequestMapping(value = "/avatar", method = RequestMethod.POST)
    public ResData changeAvatar(
            @RequestHeader(AUTH) String token,
            @RequestParam("avatar") MultipartFile avatar,
            @PathParam("type") String type
    ) {
        // TODO Image File save.
        String SSID = jwtService.getMemberSSID(token);
        Optional<Member> findSSID = memberRepository.findBySESSID(SSID);
        if (findSSID.isEmpty()) {
            return ResData.builder().message("존재하지 않는 유저입니다").data(null).result_state(false).build();
        }
        Member member = findSSID.get();
        String avatar_url = fileUpload.saveAvatarByMember(avatar, SSID);
        if (avatar_url == null) {
            return ResData.builder().message("아바타 변경에 실패했습니다").data(null).result_state(false).build();
        }
        member.setAvatar_url(avatar_url);
        memberRepository.save(member);
        return ResData.builder().message("아바타 변경에 성공했습니다").data(null).result_state(true).build();
    }

    @RequestMapping(value = "/information", method = RequestMethod.GET)
    public ResData loginInformation(
            @RequestHeader(AUTH) String token
    ) {
        if (token == null) return ResData.builder().message("로그인 토큰이 없습니다").data(null).result_state(false).build();
        boolean expired = jwtService.isExpired(token);
        if (expired) {
            String memberSSID = jwtService.getMemberSSID(token);
            Optional<Member> findSSID = memberRepository.findBySESSID(memberSSID);
            if (findSSID.isEmpty())
                return ResData.builder().message("존재하지 않는 유저입니다.").data(null).result_state(false).build();
            Member member = findSSID.get();
            Map<String, Object> result = new HashMap<>();
            result.put("member", new MemberDTO(member));
            String expiredTime = jwtService.getExpiredTime(token);
            result.put("exp", expiredTime);
            return ResData.builder().message("로그인 정보를 가져왔습니다.").data(result).result_state(true).build();
        }
        return ResData.builder().message("토큰이 만료되었습니다").data(null).result_state(false).build();
    }

    @GetMapping(value = "/me")
    public ResData getMyData(
            @RequestHeader(AUTH) String token
    ) {
        String SSID = jwtService.getMemberSSID(token);
        Optional<Member> findSSID = memberRepository.findBySESSID(SSID);
        if (findSSID.isEmpty()) {
            return ResData.builder().message("존재하지 않는 유저입니다").data(null).result_state(false).build();
        }
        Member member = findSSID.get();
        Optional<BodyData> findBodyMember = bodyDataRepository.findByMember(member);
        if (findBodyMember.isEmpty()) {
            return ResData.builder().message("내 몸 상태정보가 없습니다").data(null).result_state(false).build();
        }
        BodyData bodyData = findBodyMember.get();
        return ResData.builder().message("내 상태를 가져왔습니다.").data(new BodyDataDTO(bodyData)).result_state(true).build();
    }

    @PostMapping(value = "/create/me")
    public ResData createMyData(
            @RequestHeader(AUTH) String token,
            @RequestParam(name = "age") Integer age,
            @RequestParam(name = "weight") Double weight,
            @RequestParam(name = "height") Double height,
            @RequestParam(name = "SMM") Double SMM
    ) {
        String SSID = jwtService.getMemberSSID(token);
        Optional<Member> findSSID = memberRepository.findBySESSID(SSID);
        if (findSSID.isEmpty()) {
            return ResData.builder().message("존재하지 않는 유저입니다").data(null).result_state(false).build();
        }
        Member member = findSSID.get();
        Optional<BodyData> byMember = bodyDataRepository.findByMember(member);
        if (byMember.isPresent()) {
            return ResData.builder().message("이미 존재하는 데이터입니다.").data(null).result_state(false).build();
        }
        BodyData bodyData = new BodyData();
        bodyData.setMember(member);
        bodyData.setAge(age);
        bodyData.setHeight(height);
        bodyData.setWeight(weight);
        bodyData.setSMM(SMM);
        bodyDataRepository.save(bodyData);
        WeightLog weightLog = new WeightLog(weight, member);
        weightLogRepository.save(weightLog);
        return ResData.builder().message("해당 데이터를 저장했습니다.").data(new BodyDataDTO(bodyData)).result_state(true).build();
    }

    @PostMapping(value = "/update/me")
    public ResData updateMyData(
            @RequestHeader(AUTH) String token,
            @RequestParam(name = "age") Integer age,
            @RequestParam(name = "weight") Double weight,
            @RequestParam(name = "height") Double height,
            @RequestParam(name = "SMM") Double SMM
    ) {
        String SSID = jwtService.getMemberSSID(token);
        Optional<Member> findSSID = memberRepository.findBySESSID(SSID);
        if (findSSID.isEmpty()) {
            return ResData.builder().message("존재하지 않는 유저입니다").data(null).result_state(false).build();
        }
        Member member = findSSID.get();
        Optional<BodyData> byMember = bodyDataRepository.findByMember(member);
        if (byMember.isEmpty()) {
            return ResData.builder().message("해당 유저의 데이터가 없습니다").data(null).result_state(false).build();
        }
        BodyData bodyData = byMember.get();
        if (!bodyData.getWeight().equals(weight)) {
            WeightLog weightLog = new WeightLog(weight, member);
            weightLogRepository.save(weightLog);
        }
        bodyData.setAge(age);
        bodyData.setHeight(height);
        bodyData.setWeight(weight);
        bodyData.setSMM(SMM);
        bodyDataRepository.save(bodyData);

        return ResData.builder().message("해당 데이터를 수정했습니다.").data(new BodyDataDTO(bodyData)).result_state(true).build();
    }

    @GetMapping(value = "/weight/log")
    public ResData getWeightLog(
            @RequestHeader(AUTH) String token,
            @RequestParam(value = "start", required = false) String start,
            @RequestParam(value = "end", required = false) String end
    ) {
        String s = LocalDateTime.now().minusDays(30).toString();
        String e = LocalDateTime.now().toString();
        if (start != null) s = start;
        if (end != null) e = end;
        String SSID = jwtService.getMemberSSID(token);
        Optional<Member> findSSID = memberRepository.findBySESSID(SSID);
        if (findSSID.isEmpty()) {
            return ResData.builder().message("존재하지 않는 유저입니다").data(null).result_state(false).build();
        }
        Member member = findSSID.get();
        List<WeightLogDTO> weightLogDTOS = weightLogRepository.findByDateBetweenAndMember(LocalDateTime.parse(s), LocalDateTime.parse(e), member)
                .stream().map(WeightLogDTO::new).collect(Collectors.toList());
        return ResData.builder().message("몸무게 로그를 가져왔습니다").data(weightLogDTOS).result_state(true).build();
    }

    @RequestMapping(value = "/delete/weight/log", method = RequestMethod.DELETE)
    public ResData deleteLog(
            @RequestHeader(AUTH) String token,
            @PathParam("log_id") String log_id
    ) {
        String SSID = jwtService.getMemberSSID(token);
        Optional<Member> findSSID = memberRepository.findBySESSID(SSID);
        if (findSSID.isEmpty()) {
            return ResData.builder().message("존재하지 않는 유저입니다").data(null).result_state(false).build();
        }
        Member member = findSSID.get();
        Optional<WeightLog> findLog = weightLogRepository.findById(Long.parseLong(log_id));
        if (findLog.isEmpty())
            return ResData.builder().message("해당 로그가 존재하지않습니다.").data(null).result_state(false).build();
        WeightLog weightLog = findLog.get();
        if (!weightLog.getMember().equals(member)) {
            return ResData.builder().message("삭제할 권한이 없습니다.").data(null).result_state(false).build();
        }
        if (log_id.equals("all")) {
            List<WeightLog> byDateBetweenAndMember = weightLogRepository.findByDateBetweenAndMember(LocalDateTime.now().minusDays(30), LocalDateTime.now(), member);
            weightLogRepository.deleteAll(byDateBetweenAndMember);
            return ResData.builder().message(LocalDateTime.now().minusDays(30) + " ~ " + LocalDateTime.now() + " 로그를 삭제했습니다.").data(null).result_state(true).build();
        }
        weightLogRepository.delete(weightLog);
        return ResData.builder().message("해당 로그를 삭제했습니다.").data(null).result_state(true).build();
    }
}

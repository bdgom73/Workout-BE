package com.exerciseApp.exercise.Controller;

import com.exerciseApp.exercise.DTO.MemberDTO.MemberDTO;
import com.exerciseApp.exercise.DTO.MemberDTO.Register;
import com.exerciseApp.exercise.DTO.ResData;
import com.exerciseApp.exercise.Entity.Member;
import com.exerciseApp.exercise.Enum.MemberRank;
import com.exerciseApp.exercise.Repository.MemberRepository;
import com.exerciseApp.exercise.Service.CreateIDService;
import com.exerciseApp.exercise.Service.FileUpload;
import com.exerciseApp.exercise.Service.JWTService;
import com.exerciseApp.exercise.Service.MemberService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.Optional;

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

    private final String AUTH = "Authorization";

    public MemberController(MemberService memberService, MemberRepository memberRepository, JWTService jwtService, FileUpload fileUpload) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.jwtService = jwtService;
        this.fileUpload = fileUpload;

    }

    // 로그인
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResData Login(@RequestParam("email") String email, @RequestParam("password") String password) {
        return memberService.Login(email,password);
    }

    /* 회원가입
    *   @Register JSON
    * */
    @PostMapping(value = "/signup")
    public ResData SignUp(@RequestBody Register register){
        return memberService.SignUp(register);
    }

    // 로그인 정보
    @RequestMapping(value = "/authentication",method = RequestMethod.GET)
    public ResData AuthenticationMemberInformation(@RequestHeader(AUTH) String token){
        String SSID = jwtService.getMemberSSID(token);
        String memberSSID = jwtService.getMemberSSID(SSID);
        return memberService.AuthenticationMemberInformation(memberSSID);
    }

    // (단일)유저 찾기
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public ResData findUser(
            @PathParam("type") String type,
            @PathParam("term") String term
    ){
        if(type == null) type = "id";
        if(type.equals("nickname")){
            Optional<Member> findNickname = memberRepository.findByNickname(term);
            if(findNickname.isEmpty()) return ResData.builder().message("존재하지 않는 유저입니다").data(null).result_state(false).build();
            return ResData.builder().message("유저 검색에 성공했습니다").data(new MemberDTO(findNickname.get())).result_state(true).build();
        }else{
            Optional<Member> findID = memberRepository.findById(Long.parseLong(term));
            if(findID.isEmpty()) return ResData.builder().message("존재하지 않는 유저입니다").data(null).result_state(false).build();
            return ResData.builder().message("유저 검색에 성공했습니다").data(new MemberDTO(findID.get())).result_state(true).build();
        }
    }

    // (복수)유저 찾기
    @RequestMapping(value = "/findAll",method = RequestMethod.GET)
    public ResData findAll(
            @PathParam("condition") String condition,
            @PathParam("term") String term,
            @PathParam("size") Integer size,
            @PathParam("limit") Integer page
    ){
        StringBuilder sb ;
        if(size == null) size = 10;
        if(page == null) page = 0;
        PageRequest pageRequest = PageRequest.of(page,size);
        if(condition != null){
            if(term == null) return ResData.builder().message("조건(condition)을 선택하려면 조건값(term)값을 입력해주세요").data(new ArrayList<>()).result_state(false).build();
            switch (condition){
                case "rank" :
                    sb = new StringBuilder();
                    String msg = sb.append("page : ").append(page)
                            .append(", size :").append(size)
                            .append(", condition :").append(condition)
                            .append(", term : ").append(term).toString();
                    MemberRank memberRank = MemberRank.valueOf(term.toUpperCase());
                    Slice<Member> memberRankList = memberRepository.findSliceByMemberRank(memberRank,pageRequest);
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

    @RequestMapping(value = "/avatar",method = RequestMethod.POST)
    public ResData changeAvatar(
            @RequestHeader(AUTH) String token,
            @RequestParam("avatar") MultipartFile avatar,
            @PathParam("type") String type
    ){
        // TODO Image File save.
        String SSID = jwtService.getMemberSSID(token);
        Optional<Member> findSSID = memberRepository.findBySESSID(SSID);
        if(findSSID.isEmpty()){
            return ResData.builder().message("존재하지 않는 유저입니다").data(null).result_state(false).build();
        }
        Member member = findSSID.get();
        String avatar_url = fileUpload.saveAvatarByMember(avatar, SSID);
        if(avatar_url == null){
            return ResData.builder().message("아바타 변경에 실패했습니다").data(null).result_state(false).build();
        }
        member.setAvatarUrl(avatar_url);
        memberRepository.save(member);
        return ResData.builder().message("아바타 변경에 성공했습니다").data(null).result_state(true).build();
    }

}

package com.exerciseApp.exercise.Service;

import com.exerciseApp.exercise.DTO.ResData;
import com.exerciseApp.exercise.Entity.Member;
import com.exerciseApp.exercise.Entity.Recommend;
import com.exerciseApp.exercise.Entity.Routine;
import com.exerciseApp.exercise.Repository.MemberRepository;
import com.exerciseApp.exercise.Repository.RecommendRepository;
import com.exerciseApp.exercise.Repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final RecommendRepository recommendRepository;
    private final RoutineRepository routineRepository;
    private final MemberRepository memberRepository;

    public ResData doRecommend(Routine routine, Member member) {
        Optional<Recommend> byRoutineAndMember = recommendRepository.findByRoutineAndMember(routine, member);
        if (byRoutineAndMember.isEmpty()) {
            Recommend recommend = new Recommend();
            recommend.setIsRecommend(Boolean.TRUE);
            recommend.setMember(member);
            recommend.setRoutine(routine);
            recommendRepository.save(recommend);
            return ResData.builder().message(routine.getTitle() + "을 추천했습니다").data(Boolean.TRUE).result_state(true).build();
        } else {
            return ResData.builder().message("이미 추천했습니다.").data(null).result_state(false).build();
        }

    }

    public ResData cancelRecommend(Routine routine, Member member) {
        Optional<Recommend> byRoutineAndMember = recommendRepository.findByRoutineAndMember(routine, member);
        if (byRoutineAndMember.isEmpty()) {
            return ResData.builder().message("추천 기록이 없습니다").data(null).result_state(false).build();
        } else {
            Recommend recommend = byRoutineAndMember.get();
            recommendRepository.delete(recommend);
            return ResData.builder().message("해당 루틴의 추천을 취소했습니다.").data(Boolean.TRUE).result_state(true).build();
        }
    }

    public Long countRecommend(Routine routine) {
        Long countRecommend = recommendRepository.countByRoutineAndIsRecommend(routine, Boolean.TRUE);
        if (countRecommend == null) return 0L;
        return countRecommend;
    }


}

package com.exerciseApp.exercise.Repository;

import com.exerciseApp.exercise.Entity.Member;
import com.exerciseApp.exercise.Enum.MemberRank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByName(String name);

    Optional<Member> findByNickname(String nickname);

    Optional<Member> findBySESSID(String sessid);

    List<Member> findByMemberRank(MemberRank memberRank);

    Page<Member> findPageByMemberRank(MemberRank memberRank, Pageable pageable);

    Slice<Member> findSliceByMemberRank(MemberRank memberRank, Pageable pageable);

}

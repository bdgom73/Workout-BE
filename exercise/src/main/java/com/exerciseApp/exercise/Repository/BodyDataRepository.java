package com.exerciseApp.exercise.Repository;

import com.exerciseApp.exercise.Entity.BodyData;
import com.exerciseApp.exercise.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BodyDataRepository extends JpaRepository<BodyData, Long> {

    Optional<BodyData> findByMember(Member member);
}

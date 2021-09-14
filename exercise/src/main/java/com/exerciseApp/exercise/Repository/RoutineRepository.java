package com.exerciseApp.exercise.Repository;

import com.exerciseApp.exercise.Entity.Member;
import com.exerciseApp.exercise.Entity.Routine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoutineRepository extends JpaRepository<Routine, Long> {

    List<Routine> findByMember(Member member);

    Page<Routine> findPageByMember(Member member, Pageable pageable);

    Page<Routine> findPageByShare(Boolean share, Pageable pageable);

    Long countByShare(Boolean share);

    @Query("SELECT r FROM Routine r where r.share = true ")
    Page<Routine> findPageByRecommendRoutine(Pageable pageable);

    Page<Routine> findPageByTitleContaining(String title, Pageable pageable);

}

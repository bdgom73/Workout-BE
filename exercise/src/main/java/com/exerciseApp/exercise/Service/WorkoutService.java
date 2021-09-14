package com.exerciseApp.exercise.Service;

import com.exerciseApp.exercise.DTO.ResData;
import com.exerciseApp.exercise.DTO.WorkoutDTO.*;
import com.exerciseApp.exercise.Entity.Member;
import com.exerciseApp.exercise.Entity.Routine;
import com.exerciseApp.exercise.Entity.Volume;
import com.exerciseApp.exercise.Entity.Workout;
import com.exerciseApp.exercise.Repository.RecommendRepository;
import com.exerciseApp.exercise.Repository.RoutineRepository;
import com.exerciseApp.exercise.Repository.VolumeRepository;
import com.exerciseApp.exercise.Repository.WorkoutRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final FileUpload fileUpload;
    private final VolumeRepository volumeRepository;
    private final RoutineRepository routineRepository;
    private final RecommendRepository recommendRepository;

    private final String PATH = "/workout";

    public WorkoutService(WorkoutRepository workoutRepository, FileUpload fileUpload, VolumeRepository volumeRepository, RoutineRepository routineRepository, RecommendRepository recommendRepository) {
        this.workoutRepository = workoutRepository;
        this.fileUpload = fileUpload;
        this.volumeRepository = volumeRepository;
        this.routineRepository = routineRepository;
        this.recommendRepository = recommendRepository;
    }

    // 운동 추가
    public Long addWorkout(WorkoutRegister wo) {
        Workout workout = new Workout(wo);
        Workout result = workoutRepository.save(workout);
        return result.getId();
    }

    // 운동 추가
    public Long addWorkout(WorkoutRegister wo, MultipartFile image) {
        Workout workout = new Workout(wo);
        String img_url = fileUpload.Save(image, "/workout");
        workout.setWorkout_img(img_url);
        Workout result = workoutRepository.save(workout);
        return result.getId();
    }

    // 단일 운동가져오기
    public ResData getWorkoutDetail(Long id) {
        Optional<Workout> byWorkout = workoutRepository.findById(id);
        if (byWorkout.isEmpty())
            return ResData.builder().message("존재하지 않는 운동입니다").data(null).result_state(false).build();
        Workout workout = byWorkout.get();
        return ResData.builder().message("해당 운동정보를 가져왔습니다").data(new WorkoutDTO(workout)).result_state(true).build();
    }

    // 운동 썸네일 변경
    public ResData changeImage(MultipartFile image, Long workout_id) {
        Optional<Workout> findId = workoutRepository.findById(workout_id);
        if (findId.isEmpty()) return ResData.builder().message("존재하지 않는 운동종류입니다. 추가 후 다시시도해주세요")
                .data(null).result_state(false).build();
        Workout workout = findId.get();
        String new_url = fileUpload.Save(image, PATH);
        if (new_url == null) return ResData.builder().message("이미지 변경에 실패했습니다.")
                .data(null).result_state(false).build();
        workout.setWorkout_img(new_url);
        return ResData.builder().message("성공적으로 변경했습니다").data(new_url).result_state(true).build();
    }

    // 루틴 등록
    public Long addRoutine(RoutineRegister routineRegister, Member member) {
        Routine routine = new Routine();
        routine.setMember(member);
        routine.setTitle(routineRegister.getTitle());
        routine.setOriginalAuthor(member.getId());
        routine.setPart(routineRegister.getPart());
        Routine saveRoutine = routineRepository.save(routine);
        List<VolumeRegister> volumes = routineRegister.getVolumes();
        for (VolumeRegister volume : volumes) {
            Optional<Workout> findId = workoutRepository.findById(volume.getWorkout_id());
            if (findId.isPresent()) {
                Workout workout = findId.get();
                Volume v = new Volume(volume.getNum(), volume.getSets(), workout, saveRoutine);
                Volume saveVolume = volumeRepository.save(v);
                routine.getVolumes().add(saveVolume);
            }
        }
        return routine.getId();
    }

    // 다른사람이 만든 루틴 가져오기 (복사)
    public ResData copyRoutine(Routine routine, Member member) {
        if (!routine.getMember().equals(member)) {
            Routine newRoutine = new Routine(routine, member);
            List<Volume> volumes = routine.getVolumes();
            for (Volume volume : volumes) {
                newRoutine.getVolumes().add(volume);
            }
            routineRepository.save(newRoutine);
            for (Volume volume : volumes) {
                Volume volume1 = new Volume(volume, newRoutine);
                volumeRepository.save(volume1);
            }


            return ResData.builder().message("루틴을 가져왔습니다.").data(new RoutineDTO(newRoutine)).result_state(true).build();
        } else {
            return ResData.builder().message("자신이 만든 루틴은 가져올 수 없습니다.").data(null).result_state(false).build();
        }
    }
}

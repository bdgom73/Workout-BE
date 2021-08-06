package com.exerciseApp.exercise.Service;

import com.exerciseApp.exercise.DTO.ResData;
import com.exerciseApp.exercise.DTO.WorkoutDTO.RoutineRegister;
import com.exerciseApp.exercise.DTO.WorkoutDTO.VolumeRegister;
import com.exerciseApp.exercise.DTO.WorkoutDTO.WorkoutRegister;
import com.exerciseApp.exercise.Entity.Member;
import com.exerciseApp.exercise.Entity.Routine;
import com.exerciseApp.exercise.Entity.Volume;
import com.exerciseApp.exercise.Entity.Workout;
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

    private final String PATH = "/workout";
    public WorkoutService(WorkoutRepository workoutRepository, FileUpload fileUpload, VolumeRepository volumeRepository, RoutineRepository routineRepository) {
        this.workoutRepository = workoutRepository;
        this.fileUpload = fileUpload;
        this.volumeRepository = volumeRepository;
        this.routineRepository = routineRepository;
    }

    // 운동 추가
    public Long addWorkout(WorkoutRegister wo){
        Workout workout = new Workout(wo);
        String image_url = null;
        if(wo.getWorkout_imageFile() != null) image_url = fileUpload.Save(wo.getWorkout_imageFile(), PATH);
        workout.setWorkout_img(image_url);
        Workout result = workoutRepository.save(workout);
        return result.getId();
    }

    // 운동 썸네일 변경
    public ResData changeImage(MultipartFile image, Long workout_id){
        Optional<Workout> findId = workoutRepository.findById(workout_id);
        if(findId.isEmpty()) return ResData.builder().message("존재하지 않는 운동종류입니다. 추가 후 다시시도해주세요")
                .data(null).result_state(false).build();
        Workout workout = findId.get();
        String new_url = fileUpload.Save(image, PATH);
        if(new_url == null) return ResData.builder().message("이미지 변경에 실패했습니다.")
                .data(null).result_state(false).build();
        workout.setWorkout_img(new_url);
        return ResData.builder().message("성공적으로 변경했습니다").data(new_url).result_state(true).build();
    }

    // 루틴 등록
    public Long addRoutine(RoutineRegister routineRegister, Member member){
        Routine routine = new Routine();
        routine.setMember(member);
        routine.setTitle(routineRegister.getTitle());
        routine.setPart(routineRegister.getPart());
        Routine saveRoutine = routineRepository.save(routine);
        List<VolumeRegister> volumes = routineRegister.getVolumes();
        for (VolumeRegister volume : volumes) {
            Optional<Workout> findId = workoutRepository.findById(volume.getWorkout_id());
            if(findId.isPresent()){
                Workout workout = findId.get();
                Volume v = new Volume(volume.getNum(),volume.getSets(),volume.getOrders(),workout,saveRoutine);
                Volume saveVolume = volumeRepository.save(v);
                routine.getVolumes().add(saveVolume);
            }
        }
        return routine.getId();
    }


}

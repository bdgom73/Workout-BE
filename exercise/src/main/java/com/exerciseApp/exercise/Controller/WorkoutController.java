package com.exerciseApp.exercise.Controller;

import com.exerciseApp.exercise.DTO.ResData;
import com.exerciseApp.exercise.DTO.WorkoutDTO.RoutineRegister;
import com.exerciseApp.exercise.DTO.WorkoutDTO.VolumeRegister;
import com.exerciseApp.exercise.DTO.WorkoutDTO.WorkoutDTO;
import com.exerciseApp.exercise.DTO.WorkoutDTO.WorkoutRegister;
import com.exerciseApp.exercise.Entity.Member;
import com.exerciseApp.exercise.Entity.Routine;
import com.exerciseApp.exercise.Entity.Volume;
import com.exerciseApp.exercise.Entity.Workout;
import com.exerciseApp.exercise.Repository.MemberRepository;
import com.exerciseApp.exercise.Repository.RoutineRepository;
import com.exerciseApp.exercise.Repository.VolumeRepository;
import com.exerciseApp.exercise.Repository.WorkoutRepository;
import com.exerciseApp.exercise.Service.JWTService;
import com.exerciseApp.exercise.Service.WorkoutService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/myApi/workout")
public class WorkoutController {

    private final WorkoutService workoutService;
    private final WorkoutRepository workoutRepository;
    private final JWTService jwtService;
    private final MemberRepository memberRepository;
    private final RoutineRepository routineRepository;
    private final VolumeRepository volumeRepository;

    private final String AUTH = "Authorization";
    public WorkoutController(WorkoutService workoutService, WorkoutRepository workoutRepository, JWTService jwtService, MemberRepository memberRepository, RoutineRepository routineRepository, VolumeRepository volumeRepository) {
        this.workoutService = workoutService;
        this.workoutRepository = workoutRepository;
        this.jwtService = jwtService;
        this.memberRepository = memberRepository;
        this.routineRepository = routineRepository;
        this.volumeRepository = volumeRepository;
    }

    // 운동종류 추가
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResData addWorkout(@RequestBody WorkoutRegister wo){
        Long woID = workoutService.addWorkout(wo);
        return ResData.builder().message("성공적으로 등록했습니다")
                .data(woID).result_state(true).build();
    }

    // 단일 검색
    @RequestMapping(value = "/find",method = RequestMethod.GET)
    public ResData findWorkout(@PathParam("wid") Long wid){
        Optional<Workout> findId = workoutRepository.findById(wid);
        if(findId.isEmpty()) return ResData.builder().message("등록되어있지 않는 운동입니다")
                .data(null).result_state(false).build();
        Workout workout = findId.get();
        return ResData.builder().message(workout.getName()+" 운동정보를 가져왔습니다.")
                .data(new WorkoutDTO(workout)).result_state(true).build();
    }

    // 다중 검색
    @RequestMapping(value = "/findAll",method = RequestMethod.GET)
    public ResData findAllWorkout(
            @PathParam("condition") String condition,
            @PathParam("term") String term,
            @PathParam("size") Integer size,
            @PathParam("limit") Integer page
    ){
        StringBuilder sb = new StringBuilder();
        String msg = sb.append("page : ").append(page)
                .append(", size :").append(size)
                .append(", condition :").append(condition)
                .append(", term : ").append(term).toString();
        if(size == null) size = 10;
        if(page == null) page = 0;
        PageRequest pageRequest = PageRequest.of(page,size);

        if(condition != null){
            if(term == null) return ResData.builder().message("조건(condition)을 선택하려면 조건값(term)값을 입력해주세요").data(new ArrayList<>()).result_state(false).build();
            switch (condition){
                case "type" :
                    Slice<Workout> sliceByE_type = workoutRepository.findSliceByE_type(Integer.parseInt(term), pageRequest);
                    return ResData.builder().message(msg).data(sliceByE_type.map(WorkoutDTO::new)).result_state(true).build();
                case "part" :
                    Slice<Workout> sliceByPart = workoutRepository.findSliceByPart(term, pageRequest);
                    return ResData.builder().message(msg).data(sliceByPart.map(WorkoutDTO::new)).result_state(true).build();
                case "target" :
                    Slice<Workout> sliceByTarget = workoutRepository.findSliceByTarget(term, pageRequest);
                    return ResData.builder().message(msg).data(sliceByTarget.map(WorkoutDTO::new)).result_state(true).build();
                default:
                    return ResData.builder().message("해당 조건(condition)이 없습니다.").data(new ArrayList<>()).result_state(false).build();
            }
        }
        sb = new StringBuilder();
        msg = sb.append("page : ").append(page).append(", size : ").append(size).toString();
        Page<Workout> findAll = workoutRepository.findAll(pageRequest);
        return ResData.builder().message(msg).data(findAll.map(WorkoutDTO::new)).result_state(true).build();
    }

    // 루틴 저장
    @RequestMapping(value = "/add/routine",method = RequestMethod.POST)
    public ResData addRoutine(
            @RequestBody RoutineRegister routineRegister,
            @RequestHeader(AUTH) String token
    ){
        String SSID = jwtService.getMemberSSID(token);
        Optional<Member> findSSID = memberRepository.findBySESSID(SSID);
        if(findSSID.isEmpty()) return ResData.builder().message("존재하지 않는 유저입니다")
                .data(null).result_state(false).build();
        Member member = findSSID.get();
        Long routine_id = workoutService.addRoutine(routineRegister, member);
        return ResData.builder().message("루틴을 성공적으로 저장했습니다")
                .data(routine_id).result_state(true).build();
    }

    @RequestMapping(value = "/update/{routine}/volume",method = RequestMethod.PUT)
    public ResData addVolumeToRoutine(
            @RequestHeader(AUTH) String token,
            @RequestBody List<VolumeRegister> volumes,
            @PathVariable("routine") Long routine_id
    ) {
        String memberSSID = jwtService.getMemberSSID(token);
        Optional<Member> findSSID = memberRepository.findBySESSID(memberSSID);
        if(findSSID.isEmpty()) return ResData.builder().message("존재하지 않는 유저입니다.").data(null)
                .result_state(false).build();
        Optional<Routine> findId = routineRepository.findById(routine_id);
        if (findId.isEmpty()) return ResData.builder().message("존재하는 루틴이 아닙니다").data(null)
                .result_state(false).build();
        Routine routine = findId.get();
        if(!(routine.getMember().equals(findSSID.get()))){
            return ResData.builder().message("추가 권한이 없습니다.").data(null).result_state(false).build();
        }
        List<String> volume_name = new ArrayList<>();
        for (VolumeRegister vr : volumes) {
            Optional<Workout> findWorkout = workoutRepository.findById(vr.getWorkout_id());
            if(findWorkout.isEmpty()) return ResData.builder().message("등록되지 않은 운동입니다").data(null)
                    .result_state(false).build();
            Volume volume = new Volume(vr);
            volume.setWorkout(findWorkout.get());
            volume.setRoutine(routine);
            volumeRepository.save(volume);
            routine.getVolumes().add(volume);
            volume_name.add(findWorkout.get().getName());
        }
        Routine ro = routineRepository.save(routine);
        StringBuilder sb = new StringBuilder();
        for(String name : volume_name){sb.append(name).append(",");}
        sb.append("정보가 추가되었습니다.");
        return ResData.builder().message(sb.toString()).data(ro.getId())
                .result_state(true).build();
    }


    @RequestMapping(value = "/delete/{routine}/volume",method = RequestMethod.DELETE)
    public ResData deleteVolumeToRoutine(
            @RequestHeader(AUTH) String token,
            @RequestBody List<VolumeRegister> volumes,
            @PathVariable("routine") Long routine_id
    ){
        String memberSSID = jwtService.getMemberSSID(token);
        Optional<Member> findSSID = memberRepository.findBySESSID(memberSSID);
        if(findSSID.isEmpty()) return ResData.builder().message("존재하지 않는 유저입니다.").data(null)
                .result_state(false).build();
        Optional<Routine> findId = routineRepository.findById(routine_id);
        if (findId.isEmpty()) return ResData.builder().message("존재하는 루틴이 아닙니다").data(null)
                .result_state(false).build();
        Routine routine = findId.get();
        if(!(routine.getMember().equals(findSSID.get()))){
            return ResData.builder().message("추가 권한이 없습니다.").data(null).result_state(false).build();
        }
        List<String> volume_name = new ArrayList<>();
        for (VolumeRegister vr : volumes) {
            Optional<Workout> findWorkout = workoutRepository.findById(vr.getWorkout_id());
            if(findWorkout.isEmpty()) return ResData.builder().message("등록되지 않은 운동입니다").data(null)
                    .result_state(false).build();
            Volume volume = new Volume(vr);
            volumeRepository.delete(volume);
            routine.getVolumes().remove(volume);
            volume_name.add(findWorkout.get().getName());
        }
        Routine ro = routineRepository.save(routine);
        StringBuilder sb = new StringBuilder();
        for(String name : volume_name){sb.append(name).append(",");}
        sb.append("정보가 삭제되었습니다.");
        return ResData.builder().message(sb.toString()).data(ro.getId())
                .result_state(true).build();
    }

    @RequestMapping(value = "/delete/{routine}/routine",method = RequestMethod.DELETE)
    public ResData deleteRoutine(
            @RequestHeader(AUTH) String token,
            @PathVariable("routine") Long routine_id
    ){
        String memberSSID = jwtService.getMemberSSID(token);
        Optional<Member> findSSID = memberRepository.findBySESSID(memberSSID);
        if(findSSID.isEmpty()) return ResData.builder().message("존재하지 않는 유저입니다.").data(null)
                .result_state(false).build();
        Optional<Routine> findId = routineRepository.findById(routine_id);
        if (findId.isEmpty()) return ResData.builder().message("존재하는 루틴이 아닙니다").data(null)
                .result_state(false).build();
        Routine routine = findId.get();
        if(!(routine.getMember().equals(findSSID.get()))){
            return ResData.builder().message("추가 권한이 없습니다.").data(null).result_state(false).build();
        }
        routineRepository.delete(routine);
        return ResData.builder().message("해당 루틴을 삭제했습니다").data(null).result_state(true).build();
    }
}

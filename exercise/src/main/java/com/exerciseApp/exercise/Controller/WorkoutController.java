package com.exerciseApp.exercise.Controller;

import com.exerciseApp.exercise.DTO.ResData;
import com.exerciseApp.exercise.DTO.WorkoutDTO.*;
import com.exerciseApp.exercise.Entity.*;
import com.exerciseApp.exercise.Enum.MemberRank;
import com.exerciseApp.exercise.Repository.*;
import com.exerciseApp.exercise.Service.FileUpload;
import com.exerciseApp.exercise.Service.JWTService;
import com.exerciseApp.exercise.Service.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/myApi/workout")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;
    private final WorkoutRepository workoutRepository;
    private final JWTService jwtService;
    private final MemberRepository memberRepository;
    private final RoutineRepository routineRepository;
    private final VolumeRepository volumeRepository;
    private final DoWorkoutRepository doWorkoutRepository;
    private final RecommendRepository recommendRepository;
    private final FileUpload fileUpload;
    private final String AUTH = "Authorization";


    // 운동종류 추가
    @PostMapping(value = "/add")
    public ResData addWorkout(
            @RequestPart(value = "workout") WorkoutRegister wo,
            @RequestParam(value = "workout_image", required = false) MultipartFile workout_image
    ) {
        Long woID;
        if (workout_image.isEmpty()) {
            woID = workoutService.addWorkout(wo);
        } else {
            woID = workoutService.addWorkout(wo, workout_image);
        }

        return ResData.builder().message("성공적으로 등록했습니다")
                .data(woID).result_state(true).build();
    }

    // 운동종류 수정
    @PostMapping(value = "/update/{workout_id}")
    public ResData updateWorkout(
            @RequestHeader(AUTH) String token,
            @PathVariable("workout_id") Long workout_id,
            @RequestPart(value = "workout") WorkoutRegister wo
    ) {
        String SSID = jwtService.getMemberSSID(token);
        Optional<Member> findSSID = memberRepository.findBySESSID(SSID);
        if (findSSID.isEmpty()) return ResData.builder().message("존재하지 않는 유저입니다")
                .data(null).result_state(false).build();
        Member member = findSSID.get();
        if (!(member.getMemberRank().equals(MemberRank.ADMIN))) return ResData.builder().message("수정할 권한이 없습니다.")
                .data(null).result_state(false).build();
        Optional<Workout> byWorkout = workoutRepository.findById(workout_id);
        if (byWorkout.isEmpty()) return ResData.builder().message("등록되지 않는 운동입니다.")
                .data(null).result_state(false).build();

        Workout workout = byWorkout.get();
        workout.setName(wo.getName());
        workout.setPart(wo.getPart());
        workout.setExplanation(wo.getExplanation());
        workout.setE_type(wo.getE_type());
        workoutRepository.save(workout);
        return ResData.builder().message("성공적으로 수정했습니다")
                .data(new WorkoutDTO(workout)).result_state(true).build();
    }

    // 운동이미지수정
    @PostMapping(value = "/update/image/{workout_id}")
    public ResData updateWorkoutImage(
            @RequestHeader(AUTH) String token,
            @PathVariable("workout_id") Long workout_id,
            @RequestPart(value = "workout_image") MultipartFile workout_image
    ) {
        String SSID = jwtService.getMemberSSID(token);
        Optional<Member> findSSID = memberRepository.findBySESSID(SSID);
        if (findSSID.isEmpty()) return ResData.builder().message("존재하지 않는 유저입니다")
                .data(null).result_state(false).build();
        Member member = findSSID.get();
        if (!(member.getMemberRank().equals(MemberRank.ADMIN))) return ResData.builder().message("수정할 권한이 없습니다.")
                .data(null).result_state(false).build();
        Optional<Workout> byWorkout = workoutRepository.findById(workout_id);
        if (byWorkout.isEmpty()) return ResData.builder().message("등록되지 않는 운동입니다.")
                .data(null).result_state(false).build();

        Workout workout = byWorkout.get();
        String img_url = fileUpload.Save(workout_image, "/workout");
        workout.setWorkout_img(img_url);
        Workout saveWorkout = workoutRepository.save(workout);
        return ResData.builder().message("성공적으로 수정했습니다")
                .data(new WorkoutDTO(saveWorkout)).result_state(true).build();
    }

    // 단일 검색
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public ResData findWorkout(@PathParam("wid") Long wid) {
        Optional<Workout> findId = workoutRepository.findById(wid);
        if (findId.isEmpty()) return ResData.builder().message("등록되어있지 않는 운동입니다")
                .data(null).result_state(false).build();
        Workout workout = findId.get();
        return ResData.builder().message(workout.getName() + " 운동정보를 가져왔습니다.")
                .data(new WorkoutDTO(workout)).result_state(true).build();
    }

    // 다중 검색
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public ResData findAllWorkout(
            @PathParam("condition") String condition,
            @PathParam("term") String term,
            @PathParam("size") Integer size,
            @PathParam("page") Integer page
    ) {
        StringBuilder sb = new StringBuilder();

        String msg = sb.append("page : ").append(page)
                .append(", size :").append(size)
                .append(", condition :").append(condition)
                .append(", term : ").append(term).toString();

        if (size == null) size = 10;
        if (page == null) page = 0;
        PageRequest pageRequest = PageRequest.of(page, size);

        if (condition != null) {
            if (term == null)
                return ResData.builder().message("조건(condition)을 선택하려면 조건값(term)값을 입력해주세요").data(new ArrayList<>()).result_state(false).build();
            switch (condition) {
                case "type":
                    Page<Workout> sliceByE_type = workoutRepository.findPageByE_type(Integer.parseInt(term), pageRequest);
                    return ResData.builder().message(msg).data(sliceByE_type.map(WorkoutDTO::new)).result_state(true).build();
                case "part":
                    Page<Workout> sliceByPart = workoutRepository.findPageByPart(term, pageRequest);
                    return ResData.builder().message(msg).data(sliceByPart.map(WorkoutDTO::new)).result_state(true).build();
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
    @RequestMapping(value = "/add/routine", method = RequestMethod.POST)
    public ResData addRoutine(
            @RequestBody RoutineRegister routineRegister,
            @RequestHeader(AUTH) String token
    ) {
        String SSID = jwtService.getMemberSSID(token);
        Optional<Member> findSSID = memberRepository.findBySESSID(SSID);
        if (findSSID.isEmpty()) return ResData.builder().message("존재하지 않는 유저입니다")
                .data(null).result_state(false).build();
        Member member = findSSID.get();
        Long routine_id = workoutService.addRoutine(routineRegister, member);
        return ResData.builder().message("루틴을 성공적으로 저장했습니다")
                .data(routine_id).result_state(true).build();
    }

    // 루틴 수정
    @RequestMapping(value = "/update/routine/{routine}", method = RequestMethod.PUT)
    public ResData addRoutine(
            @RequestHeader(AUTH) String token,
            @PathVariable("routine") String routine_id,
            @RequestBody RoutineRegister routineRegister
    ) {
        String SSID = jwtService.getMemberSSID(token);
        Optional<Member> findSSID = memberRepository.findBySESSID(SSID);
        if (findSSID.isEmpty()) return ResData.builder().message("존재하지 않는 유저입니다")
                .data(null).result_state(false).build();
        Optional<Routine> byId = routineRepository.findById(Long.parseLong(routine_id));
        if (byId.isEmpty()) return ResData.builder().message("존재하지 않는 루틴입니다.")
                .data(null).result_state(false).build();
        Routine routine = byId.get();
        routine.setTitle(routineRegister.getTitle());
        routine.setPart(routineRegister.getPart());
        routineRepository.save(routine);
        if (routineRegister.getVolumes() != null) {
            for (VolumeRegister vr : routineRegister.getVolumes()) {
                Optional<Volume> byVolumeId = volumeRepository.findById(vr.getId());
                if (byVolumeId.isEmpty()) continue;
                Volume volume = byVolumeId.get();
                volume.setNum(vr.getNum());
                volume.setSets(vr.getSets());
                volumeRepository.save(volume);
            }
        }
        return ResData.builder().message("성공적으로 변경하였습니다.")
                .data(new RoutineDTO(routine)).result_state(true).build();
    }

    // 해당 루틴에 볼륨추가
    @RequestMapping(value = "/add/{routine}/volume", method = RequestMethod.POST)
    public ResData addVolumeToRoutine(
            @RequestHeader(AUTH) String token,
            @RequestBody List<VolumeRegister> volumes,
            @PathVariable("routine") Long routine_id
    ) {
        String memberSSID = jwtService.getMemberSSID(token);
        Optional<Member> findSSID = memberRepository.findBySESSID(memberSSID);
        if (findSSID.isEmpty()) return ResData.builder().message("존재하지 않는 유저입니다.").data(null)
                .result_state(false).build();
        Optional<Routine> findId = routineRepository.findById(routine_id);
        if (findId.isEmpty()) return ResData.builder().message("존재하는 루틴이 아닙니다").data(null)
                .result_state(false).build();
        Routine routine = findId.get();
        if (!(routine.getMember().equals(findSSID.get()))) {
            return ResData.builder().message("추가 권한이 없습니다.").data(null).result_state(false).build();
        }

        for (VolumeRegister vr : volumes) {
            Optional<Workout> findWorkout = workoutRepository.findById(vr.getWorkout_id());
            if (findWorkout.isEmpty()) return ResData.builder().message("등록되지 않은 운동입니다").data(null)
                    .result_state(false).build();
            Volume volume = new Volume(vr);
            volume.setWorkout(findWorkout.get());
            volume.setRoutine(routine);
            volumeRepository.save(volume);
            routine.getVolumes().add(volume);
        }
        Routine rot = routineRepository.save(routine);
        List<VolumeDTO> volumeDTOS = rot.getVolumes().stream().map(VolumeDTO::new).collect(Collectors.toList());
        return ResData.builder().message("추가했습니다.").data(volumeDTOS)
                .result_state(true).build();
    }

    // 해당 루틴에 볼륨추가
    @RequestMapping(value = "/update/{routine}/volume", method = RequestMethod.PUT)
    public ResData updateVolumeToRoutine(
            @RequestHeader(AUTH) String token,
            @RequestBody List<VolumeRegister> volumes_ids,
            @PathVariable("routine") Long routine_id
    ) {
        String memberSSID = jwtService.getMemberSSID(token);
        Optional<Member> findSSID = memberRepository.findBySESSID(memberSSID);
        if (findSSID.isEmpty()) return ResData.builder().message("존재하지 않는 유저입니다.").data(null)
                .result_state(false).build();
        Optional<Routine> findId = routineRepository.findById(routine_id);
        if (findId.isEmpty()) return ResData.builder().message("존재하는 루틴이 아닙니다").data(null)
                .result_state(false).build();
        Routine routine = findId.get();
        if (!(routine.getMember().equals(findSSID.get()))) {
            return ResData.builder().message("추가 권한이 없습니다.").data(null).result_state(false).build();
        }

        for (VolumeRegister vr : volumes_ids) {
            Optional<Volume> byId = volumeRepository.findById(vr.getId());
            if (byId.isEmpty()) continue;
            Volume volume = byId.get();
            Optional<Workout> byWorkout = workoutRepository.findById(vr.getWorkout_id());
            if (byWorkout.isEmpty()) continue;
            Workout workout = byWorkout.get();
            volume.setWorkout(workout);
            volume.setNum(vr.getNum());
            volume.setSets(vr.getSets());
            volumeRepository.save(volume);
        }

        List<VolumeDTO> volumeDTOS = volumeRepository.findByRoutine(routine).stream().map(VolumeDTO::new).collect(Collectors.toList());

        return ResData.builder().message("성공적으로 변경했습니다.").data(volumeDTOS)
                .result_state(true).build();
    }

    // 해당 루틴 상세내역 가져오기
    @RequestMapping(value = "/get/routine/{routine_id}", method = RequestMethod.GET)
    public ResData getRoutineDetail(
            @RequestHeader(AUTH) String token,
            @PathVariable("routine_id") String routine_id
    ) {
        String memberSSID = jwtService.getMemberSSID(token);
        Optional<Member> findSSID = memberRepository.findBySESSID(memberSSID);
        if (findSSID.isEmpty()) return ResData.builder().message("존재하지 않는 유저입니다.").data(null)
                .result_state(false).build();
        Member member = findSSID.get();
        Optional<Routine> byId = routineRepository.findById(Long.parseLong(routine_id));
        if (byId.isEmpty()) return ResData.builder().message("존재하지 않는 루틴입니다.").data(null)
                .result_state(false).build();

        Routine routine = byId.get();
        boolean equals = routine.getMember().equals(member);
        if (!equals) {
            return ResData.builder().message("본인이 만든 루틴이 아닙니다.").data(null)
                    .result_state(false).build();
        }
        return ResData.builder().message("해당데이터를 불러왔습니다").data(new RoutineDTO(routine))
                .result_state(true).build();
    }

    // 루틴 전체가져오기
    @RequestMapping(value = "/get/routine", method = RequestMethod.POST)
    public ResData getRoutine(
            @RequestHeader(AUTH) String token,
            @RequestBody List<Long> idList
    ) {
        String memberSSID = jwtService.getMemberSSID(token);
        Optional<Member> findSSID = memberRepository.findBySESSID(memberSSID);
        if (findSSID.isEmpty()) return ResData.builder().message("존재하지 않는 유저입니다.").data(null)
                .result_state(false).build();

        List<RoutineDTO> routines = new ArrayList<>();
        for (Long id : idList) {
            Optional<Routine> fid = routineRepository.findById(id);
            if (fid.isEmpty()) continue;
            Routine routine = fid.get();
            routines.add(new RoutineDTO(routine));
        }
        return ResData.builder().message("해당데이터를 불러왔습니다").data(routines)
                .result_state(true).build();
    }

    // 공유변경
    @RequestMapping(value = "/update/routine/{routine}/share={share}", method = RequestMethod.GET)
    public ResData changeShare(
            @RequestHeader(AUTH) String token,
            @PathVariable("routine") Long routine_id,
            @PathVariable("share") String share
    ) {
        String memberSSID = jwtService.getMemberSSID(token);
        Optional<Member> findSSID = memberRepository.findBySESSID(memberSSID);
        if (findSSID.isEmpty()) return ResData.builder().message("존재하지 않는 유저입니다.").data(null)
                .result_state(false).build();
        Optional<Routine> byId = routineRepository.findById(routine_id);
        if (byId.isEmpty()) ResData.builder().message("해당 루틴이 존재하지 않습니다.").data(null)
                .result_state(false).build();
        Routine routine = byId.get();
        routine.setShare(Boolean.valueOf(share));
        routineRepository.save(routine);
        return ResData.builder().message("공유 상태를 " + Boolean.valueOf(share) + "로 변경하였습니다.").data(new RoutineDTO(routine))
                .result_state(true).build();
    }

    // 루틴 복사
    @RequestMapping(value = "/copy/routine/{routine}", method = RequestMethod.POST)
    public ResData copyRoutine(
            @RequestHeader(AUTH) String token,
            @PathVariable("routine") Long routine_id
    ) {
        String memberSSID = jwtService.getMemberSSID(token);
        Optional<Member> findSSID = memberRepository.findBySESSID(memberSSID);
        if (findSSID.isEmpty()) return ResData.builder().message("존재하지 않는 유저입니다.").data(null)
                .result_state(false).build();
        Member member = findSSID.get();
        Optional<Routine> byId = routineRepository.findById(routine_id);
        if (byId.isEmpty()) ResData.builder().message("해당 루틴이 존재하지 않습니다.").data(null)
                .result_state(false).build();
        Routine routine = byId.get();
        return workoutService.copyRoutine(routine, member);

    }

    // 해당 루틴에 볼륨삭제
    @RequestMapping(value = "/delete/{routine}/volume", method = RequestMethod.POST)
    public ResData deleteVolumeToRoutine(
            @RequestHeader(AUTH) String token,
            @RequestBody List<String> volumes,
            @PathVariable("routine") Long routine_id
    ) {
        String memberSSID = jwtService.getMemberSSID(token);
        Optional<Member> findSSID = memberRepository.findBySESSID(memberSSID);
        if (findSSID.isEmpty()) return ResData.builder().message("존재하지 않는 유저입니다.").data(null)
                .result_state(false).build();
        Optional<Routine> findId = routineRepository.findById(routine_id);
        if (findId.isEmpty()) return ResData.builder().message("존재하는 루틴이 아닙니다").data(null)
                .result_state(false).build();
        Routine routine = findId.get();
        if (!(routine.getMember().equals(findSSID.get()))) {
            return ResData.builder().message("추가 권한이 없습니다.").data(null).result_state(false).build();
        }
        for (String vr : volumes) {
            Optional<Volume> byId = volumeRepository.findById(Long.parseLong(vr));
            if (byId.isEmpty()) return ResData.builder().message("해당 볼륨이 없습니다").data(null).result_state(false).build();
            Volume volume = byId.get();
            volumeRepository.delete(volume);
            routine.getVolumes().remove(volume);
        }
        return ResData.builder().message("해당 볼륨이 삭제되었습니다.").data(new RoutineDTO(routine))
                .result_state(true).build();
    }

    // 해당 루틴 삭제
    @RequestMapping(value = "/delete/{routine}/routine", method = RequestMethod.DELETE)
    public ResData deleteRoutine(
            @RequestHeader(AUTH) String token,
            @PathVariable("routine") Long routine_id
    ) {
        String memberSSID = jwtService.getMemberSSID(token);
        Optional<Member> findSSID = memberRepository.findBySESSID(memberSSID);
        if (findSSID.isEmpty()) return ResData.builder().message("존재하지 않는 유저입니다.").data(null)
                .result_state(false).build();
        Member member = findSSID.get();
        Optional<Routine> findId = routineRepository.findById(routine_id);
        if (findId.isEmpty()) return ResData.builder().message("존재하는 루틴이 아닙니다").data(null)
                .result_state(false).build();
        Routine routine = findId.get();
        if (!(routine.getMember().equals(findSSID.get()))) {
            return ResData.builder().message("추가 권한이 없습니다.").data(null).result_state(false).build();
        }
        Optional<Recommend> byRoutine = recommendRepository.findByRoutine(routine);
        Recommend recommend = byRoutine.get();
        recommendRepository.delete(recommend);
        routineRepository.delete(routine);
        List<Routine> byMember = routineRepository.findByMember(member);
        List<RoutineDTO> routines = byMember.stream().map(RoutineDTO::new).collect(Collectors.toList());
        return ResData.builder().message("해당 루틴을 삭제했습니다").data(routines).result_state(true).build();
    }

    @RequestMapping(value = "/get/list/routine", method = RequestMethod.GET)
    public ResData getRoutineList(
            @RequestHeader(AUTH) String token
    ) {
        String memberSSID = jwtService.getMemberSSID(token);
        Optional<Member> findSSID = memberRepository.findBySESSID(memberSSID);
        if (findSSID.isEmpty()) return ResData.builder().message("존재하지 않는 유저입니다.").data(null)
                .result_state(false).build();
        Member member = findSSID.get();

        List<Routine> byMember = routineRepository.findByMember(member);

        List<RoutineDTO> routines = byMember.stream().map(RoutineDTO::new).collect(Collectors.toList());
        return ResData.builder().message("성공적으로 데이터를 불러왔습니다").data(routines)
                .result_state(true).build();
    }

    @RequestMapping(value = "/get/list/isWorkout", method = RequestMethod.GET)
    public ResData getIsWorkoutList(
            @RequestHeader(AUTH) String token,
            @PathParam("start_date") String start_date,
            @PathParam("end_date") String end_date
    ) {
        String memberSSID = jwtService.getMemberSSID(token);
        Optional<Member> findSSID = memberRepository.findBySESSID(memberSSID);
        if (findSSID.isEmpty()) return ResData.builder().message("존재하지 않는 유저입니다.").data(null)
                .result_state(false).build();
        Member member = findSSID.get();
        List<DoWorkout> do_workout_list = doWorkoutRepository.findByMemberAndDateBetween(member, LocalDate.parse(start_date), LocalDate.parse(end_date));
        List<DoWorkoutDTO> collect = do_workout_list.stream().map(DoWorkoutDTO::new).collect(Collectors.toList());
        return ResData.builder().message("성공적으로 데이터를 가져왔습니다").data(collect)
                .result_state(true)
                .build();
    }


    @RequestMapping(value = "/{date}/change/isWorkout={isWorkout}", method = RequestMethod.GET)
    public ResData ChangeWorkout(
            @RequestHeader(AUTH) String token,
            @PathVariable("date") String date,
            @PathVariable("isWorkout") Boolean isWorkout
    ) {
        String memberSSID = jwtService.getMemberSSID(token);
        Optional<Member> findSSID = memberRepository.findBySESSID(memberSSID);
        if (findSSID.isEmpty()) return ResData.builder().message("존재하지 않는 유저입니다.").data(null)
                .result_state(false).build();
        Member member = findSSID.get();
        Optional<DoWorkout> findIsWorkout = doWorkoutRepository.findByMemberAndDate(member, LocalDate.parse(date));
        if (findIsWorkout.isEmpty()) {
            DoWorkout doWorkout = new DoWorkout();
            doWorkout.setIsWorkout(isWorkout);
            doWorkout.setDate(LocalDate.parse(date));
            doWorkout.setMember(member);
            DoWorkout dw = doWorkoutRepository.save(doWorkout);
            return ResData.builder().message("성공적으로 데이터를 가져왔습니다").data(new DoWorkoutDTO(dw)).result_state(true).build();
        } else {
            DoWorkout doWorkout = findIsWorkout.get();
            doWorkout.setIsWorkout(isWorkout);
            doWorkoutRepository.save(doWorkout);

            return ResData.builder().message("성공적으로 데이터를 가져왔습니다").data(new DoWorkoutDTO(doWorkout)).result_state(true).build();
        }

    }

    // 공유된 루틴들 가져오기
    @RequestMapping(value = "/get/list/share/routine", method = RequestMethod.GET)
    public ResData getShareRoutine(
            @PathParam("size") Integer size,
            @PathParam("page") Integer page,
            @PathParam("type") String type
    ) {
        if (size == null) size = 10;
        if (page == null) page = 0;

        Sort Cus_sort = null;
        Long total = routineRepository.countByShare(Boolean.TRUE);

        switch (type) {
            case "recommend":
                PageRequest pageRequest = PageRequest.of(page, size);
                Page<Routine> pageByRecommendRoutine = routineRepository.findPageByRecommendRoutine(pageRequest);
                List<GoodRoutineDTO> collect = pageByRecommendRoutine.map(GoodRoutineDTO::new).get().collect(Collectors.toList());
                collect.sort(Comparator.comparing(GoodRoutineDTO::getRecommend).reversed());
                Map<String, Object> resData_data = new HashMap<>();
                resData_data.put("count", total);
                resData_data.put("content", collect);
                return ResData.builder().message("공유중인 루틴을 가져왔습니다").data(resData_data).result_state(true).build();
            case "desc":
                Cus_sort = Sort.by("createdDate").descending();
                break;
            default:
                Cus_sort = Sort.by("createdDate").ascending();
                break;
        }

        PageRequest pageRequest = PageRequest.of(page, size, Cus_sort);

        Page<Routine> pageByShare = routineRepository.findPageByShare(Boolean.TRUE, pageRequest);
        List<Routine> routineList = pageByShare.get().collect(Collectors.toList());
        List<GoodRoutineDTO> returnDataList = new ArrayList<>();

        for (Routine routine : routineList) {
            Long count = recommendRepository.countByRoutineAndIsRecommend(routine, Boolean.TRUE);
            GoodRoutineDTO goodRoutineDTO = new GoodRoutineDTO(routine, count);
            returnDataList.add(goodRoutineDTO);
        }
        Map<String, Object> resData_data = new HashMap<>();
        resData_data.put("count", total);
        resData_data.put("content", returnDataList);
        return ResData.builder().message("공유중인 루틴을 가져왔습니다").data(resData_data).result_state(true).build();

    }

    // 공유된 루틴(단일) 가져오기
    @RequestMapping(value = "/get/share/routine/{routine}", method = RequestMethod.GET)
    public ResData getShareRoutine(@PathVariable("routine") String routine_id) {

        Optional<Routine> byId = routineRepository.findById(Long.parseLong(routine_id));
        if (byId.isEmpty()) return ResData.builder().message("존재하지 않은 루틴입니다.").data(null).result_state(false).build();
        Routine routine = byId.get();
        if (!routine.getShare())
            return ResData.builder().message("공유되어있지 않은 루틴입니다.").data(null).result_state(false).build();

        Long recommend = recommendRepository.countByRoutineAndIsRecommend(routine, Boolean.TRUE);
        return ResData.builder().message("공유중인 루틴을 가져왔습니다").data(new ShareRoutineDTO(routine, recommend)).result_state(true).build();

    }
}

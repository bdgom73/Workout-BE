package com.exerciseApp.exercise.Service;

import com.exerciseApp.exercise.DTO.WorkoutDTO.RoutineNvDTO;
import com.exerciseApp.exercise.Repository.RoutineRepository;
import com.exerciseApp.exercise.Repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final RoutineRepository routineRepository;
    private final SearchRepository searchRepository;

    public Map<String, Object> search(List<String> keyword, PageRequest pageRequest) {
        Long total = searchRepository.countByTitle(keyword);
        List<RoutineNvDTO> byKeyword = searchRepository.findByKeyword(keyword, pageRequest).stream().map(RoutineNvDTO::new).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("content", byKeyword);
        return result;
    }

    public Map<String, Object> search(List<String> keyword) {
        List<RoutineNvDTO> searchTitle = searchRepository.findByKeyword(keyword).stream().map(RoutineNvDTO::new).collect(Collectors.toList());
        Map<String, Object> result = new HashMap<>();
        result.put("content", searchTitle);
        return result;
    }

    public Map<String, Object> search(String keyword) {
        List<String> keywords = new ArrayList<>();
        keywords.add(keyword);
        List<RoutineNvDTO> searchTitle = searchRepository.findSearchTitle(keywords).stream().map(RoutineNvDTO::new).collect(Collectors.toList());
        Map<String, Object> result = new HashMap<>();
        result.put("content", searchTitle);
        return result;
    }

    public Map<String, Object> search(String keyword, PageRequest pageRequest) {
        List<String> keywords = new ArrayList<>();
        keywords.add(keyword);
        Long total = searchRepository.countByTitle(keywords);
        List<RoutineNvDTO> byKeyword = searchRepository.findSearchTitle(keywords, pageRequest).stream().map(RoutineNvDTO::new).collect(Collectors.toList());
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("content", byKeyword);
        return result;
    }
}

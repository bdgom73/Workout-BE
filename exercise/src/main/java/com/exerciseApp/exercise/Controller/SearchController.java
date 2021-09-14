package com.exerciseApp.exercise.Controller;

import com.exerciseApp.exercise.DTO.ResData;
import com.exerciseApp.exercise.Service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping(value = "/myApi")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResData search(
            @RequestParam("keywords") List<String> keywords,
            @PathParam("size") Integer size,
            @PathParam("page") Integer page
    ) {
        if (size == 0) {
            return ResData.builder().message("검색 데이터를 가져왔습니다.").data(searchService.search(keywords)).result_state(true)
                    .build();
        }

        PageRequest pageRequest = PageRequest.of(page, size);
        return ResData.builder().message("검색 데이터를 가져왔습니다.").data(searchService.search(keywords, pageRequest)).result_state(true)
                .build();
    }
}

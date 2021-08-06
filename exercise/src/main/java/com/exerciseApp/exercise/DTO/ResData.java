package com.exerciseApp.exercise.DTO;

import lombok.*;

@Data @Getter @Setter
@Builder
public class ResData {

    private String message;
    private Object data;
    private boolean result_state;

}

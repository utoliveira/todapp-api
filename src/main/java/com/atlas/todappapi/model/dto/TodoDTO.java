package com.atlas.todappapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor @AllArgsConstructor
public class TodoDTO {

    @Getter
    private Long id;

    @Getter
    private String text;

    @Getter
    private boolean complete;
}

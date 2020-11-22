package com.atlas.todappapi.model.request;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

import static com.atlas.todappapi.consts.TodappRequestConsts.TODO_TEXT_MAX_LENGTH;
import static com.atlas.todappapi.consts.TodappRequestConsts.TODO_TEXT_MIN_LENGTH;

@Builder
@NoArgsConstructor @AllArgsConstructor
public class TodoAddRequest {

    @NotBlank
    @Length(max = TODO_TEXT_MAX_LENGTH, min = TODO_TEXT_MIN_LENGTH)
    @Getter @Setter
    private String text;

}

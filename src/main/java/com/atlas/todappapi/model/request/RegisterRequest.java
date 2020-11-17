package com.atlas.todappapi.model.request;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.atlas.todappapi.consts.TodappRequestConsts.*;

@Builder
@NoArgsConstructor @AllArgsConstructor
public class RegisterRequest {

    @NotBlank
    @Length(min = 1, max = 100)
    @Getter @Setter
    private String name;

    @Getter @Setter
    @NotBlank
    @Length(min = PASSWORD_MIN_LENGTH, max=PASSWORD_MAX_LENGTH)
    private String password;

    @Getter @Setter
    @NotBlank
    @Pattern(regexp = EMAIL_REGEX, message = "Email pattern not allowed")
    @Length(max = EMAIL_MAX_LENGTH)
    private String email;
}

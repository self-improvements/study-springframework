package io.github.imsejin.springstudy.validation.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;

@Getter
@Setter
@ToString
@Builder
public class User {

    @NotNull
    @Positive
    private final Long id;

    @NotBlank
    private String name;

    @Max(150)
    @Positive
    private Integer age;

    @Email
    private String email;

}

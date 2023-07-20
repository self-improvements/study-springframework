package io.github.imsejin.study.springframework.core.validation.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

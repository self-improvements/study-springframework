package io.github.imsejin.study.springframework.core.validation.model;

import io.github.imsejin.study.springframework.core.validation.constraint.AnyOf;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AnyOf(constraint = NotBlank.class, fieldNames = {"modelName", "brandName"})
public class Camera {

    @NotNull
    @Min(10000000)
    @Max(99999999)
    private Long serialNumber;

    @NotBlank
    private String modelName;

    @Size(min = 11, max = 11)
    @Pattern(regexp = "^[A-Z]+-\\d+$")
    private String brandName;

    private LocalDate manufacturedAt;

}

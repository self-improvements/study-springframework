package io.github.imsejin.study.springframework.core.validation.model;

import io.github.imsejin.study.springframework.core.validation.constraint.AnyOf;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    private String modelName;

    private String brandName;

    @Valid
    @NotNull
    private Lens lens;

    private LocalDate manufacturedAt;

}

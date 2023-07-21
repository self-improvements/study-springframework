package io.github.imsejin.study.springframework.core.validation.model;

import io.github.imsejin.study.springframework.core.validation.constraint.AnyOf;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AnyOf(constraint = NotNull.class, fieldNames = {"serialNumber", "manufacturedAt"})
@AnyOf(constraint = NotEmpty.class, fieldNames = {"modelName", "brandName"})
public class Lens {

    @Pattern(regexp = "[A-Z]{0,4}")
    private Long serialNumber;

    private String modelName;

    private String brandName;

    private LocalDate manufacturedAt;

}

package io.github.imsejin.study.springframework.core.validation.model;

import io.github.imsejin.study.springframework.core.validation.constraint.AnyOf;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AnyOf(constraint = NotNull.class, fieldNames = {"serialNumber", "manufacturedAt"})
@AnyOf(constraint = NotEmpty.class, fieldNames = {"modelName", "brandName"})
public class Lens {

    private Long serialNumber;

    private String modelName;

    private String brandName;

    private LocalDate manufacturedAt;

}

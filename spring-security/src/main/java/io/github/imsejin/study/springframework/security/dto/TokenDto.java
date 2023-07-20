package io.github.imsejin.study.springframework.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {

    @NotBlank
    private String token;

}

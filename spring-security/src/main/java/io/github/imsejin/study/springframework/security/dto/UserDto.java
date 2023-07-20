package io.github.imsejin.study.springframework.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @JsonProperty(access = Access.WRITE_ONLY)
    @NotBlank
    @Size(min = 3, max = 50)
    private String password;

    @NotNull
    @Size(min = 3, max = 50)
    private String nickname;

}

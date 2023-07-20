package io.github.imsejin.study.springframework.security.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Getter
@ToString
@ConfigurationProperties("jwt")
@RequiredArgsConstructor(onConstructor = @__(@ConstructorBinding))
public class JwtProperties {

    private final String header;

    private final String secret;

    private final Long tokenValidityInSeconds;

}

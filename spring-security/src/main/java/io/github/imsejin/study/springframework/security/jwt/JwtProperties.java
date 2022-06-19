package io.github.imsejin.study.springframework.security.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ToString
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties("jwt")
public class JwtProperties {

    private final String header;

    private final String secret;

    private final Long tokenValidityInSeconds;

}

package io.github.imsejin.study.springframework.security.controller;

import io.github.imsejin.study.springframework.security.dto.LoginDto;
import io.github.imsejin.study.springframework.security.dto.TokenDto;
import io.github.imsejin.study.springframework.security.jwt.TokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apis")
@RequiredArgsConstructor
public class AuthController {

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("authentication")
    ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = this.authenticationManagerBuilder.getObject()
                // Call CustomUserDetailsService.loadUserByUsername.
                .authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = this.tokenProvider.createToken(authentication);

        // 토큰을 헤더와 바디에 담을 수 있다.
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(this.tokenProvider.getJwtProperties().getHeader(), "Bearer " + jwt);

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(new TokenDto(jwt));
    }

}

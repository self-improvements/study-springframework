package io.github.imsejin.study.springframework.security.config;

import io.github.imsejin.study.springframework.security.jwt.JwtAccessDeniedHandler;
import io.github.imsejin.study.springframework.security.jwt.JwtAuthenticationEntryPoint;
import io.github.imsejin.study.springframework.security.jwt.JwtProperties;
import io.github.imsejin.study.springframework.security.jwt.JwtSecurityConfigurer;
import io.github.imsejin.study.springframework.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true) // For @PreAuthorized
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtProperties jwtProperties;

    private final TokenProvider tokenProvider;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/h2-console/**", "/favicon.ico");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.exceptionHandling()
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .accessDeniedHandler(new JwtAccessDeniedHandler());

        // For H2 console.
        http.headers().frameOptions().sameOrigin();

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers("/apis/greeting").permitAll()
                .antMatchers("/apis/signup", "/apis/authentication").permitAll()
                .anyRequest().authenticated();

        http.apply(new JwtSecurityConfigurer(this.jwtProperties, this.tokenProvider));
    }

    @Bean
    @Primary
    static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

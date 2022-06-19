package io.github.imsejin.study.springframework.security.service;

import io.github.imsejin.study.springframework.security.entity.User;
import io.github.imsejin.study.springframework.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findWithAuthoritiesByUsername(username)
                .map(it -> createUser(username, it))
                .orElseThrow(() -> new UsernameNotFoundException(username + " is not found"));
    }

    private org.springframework.security.core.userdetails.User createUser(String username, User user) {
        if (!user.isActivated()) {
            throw new RuntimeException(username + " is not activated");
        }

        List<GrantedAuthority> authorities = user.getAuthorities().stream()
                .map(it -> new SimpleGrantedAuthority(it.getAuthorityName()))
                .collect(toList());

        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }

}

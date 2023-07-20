package io.github.imsejin.study.springframework.security.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "USER")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @JsonIgnore
    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "USERNAME", length = 50, unique = true)
    private String username;

    @Column(name = "PASSWORD", length = 100)
    private String password;

    @Column(name = "NICKNAME", length = 50)
    private String nickname;

    @Column(name = "ACTIVATED", nullable = false)
    private boolean activated;

    @ManyToMany
    @JoinTable(
            name = "USER_AUTHORITY",
            joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "AUTHORITY_NAME", referencedColumnName = "AUTHORITY_NAME")
    )
    private Set<Authority> authorities;

}

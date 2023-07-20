package io.github.imsejin.study.springframework.security.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "AUTHORITY")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Authority {

    @Id
    @Column(name = "AUTHORITY_NAME", length = 50)
    private String authorityName;

}

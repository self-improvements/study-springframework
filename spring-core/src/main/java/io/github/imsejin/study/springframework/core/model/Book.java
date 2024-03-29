package io.github.imsejin.study.springframework.core.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Book {

    private final Integer id;

    private String name;

    private double price;

    private LocalDate publishedAt;

}

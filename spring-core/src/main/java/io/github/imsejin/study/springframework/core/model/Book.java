package io.github.imsejin.study.springframework.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

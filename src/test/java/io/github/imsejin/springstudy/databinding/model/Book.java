package io.github.imsejin.springstudy.databinding.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class Book {

    private final Long id;

    private String name;

    @JsonIgnore
    private double price;

    private LocalDate publishedAt;

}

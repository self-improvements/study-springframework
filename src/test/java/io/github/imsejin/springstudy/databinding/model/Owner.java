package io.github.imsejin.springstudy.databinding.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Owner {

    private final Integer id;

    private String name;

}

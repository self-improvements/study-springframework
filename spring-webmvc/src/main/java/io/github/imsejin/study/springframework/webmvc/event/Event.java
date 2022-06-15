package io.github.imsejin.study.springframework.webmvc.event;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
public class Event {

    private long id;

    private String name;

    private int capacity;

    private LocalDate startAt;

    private LocalDate endAt;

}

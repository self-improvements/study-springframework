package io.github.imsejin.study.springframework.webmvc.event;

import io.github.imsejin.study.springframework.annotation.Marking;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Marking
@Service
public class EventService {

    public List<Event> getEvents() {
        Event springFramework = Event.builder()
                .id(123456789)
                .name("Spring framework")
                .startAt(LocalDate.of(2021, Month.APRIL, 1))
                .endAt(LocalDate.of(2021, Month.APRIL, 30))
                .capacity(49).build();
        Event hibernate = Event.builder()
                .id(987654321)
                .name("Hibernate")
                .startAt(LocalDate.of(2021, Month.MAY, 1))
                .endAt(LocalDate.of(2021, Month.MAY, 15))
                .capacity(30).build();

        return List.of(springFramework, hibernate);
    }

}

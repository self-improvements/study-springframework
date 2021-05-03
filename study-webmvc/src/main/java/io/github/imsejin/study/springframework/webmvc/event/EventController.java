package io.github.imsejin.study.springframework.webmvc.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public String events(Model model) {
        log.info("Events: {}", eventService.getEvents());

        model.addAttribute("events", eventService.getEvents());
        return "events";
    }

}

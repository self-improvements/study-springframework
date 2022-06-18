package io.github.imsejin.study.springframework.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apis")
public class WelcomeController {

    @GetMapping("greeting")
    ResponseEntity<String> greeting() {
        return ResponseEntity.ok("hello");
    }

}

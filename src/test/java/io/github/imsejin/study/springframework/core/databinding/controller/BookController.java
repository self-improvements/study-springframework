package io.github.imsejin.study.springframework.core.databinding.controller;

import io.github.imsejin.study.springframework.core.databinding.model.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("books")
public class BookController {

    @GetMapping
    public List<Book> findBooks() {
        return new ArrayList<>();
    }

    @GetMapping("{name}")
    public Object getBookByName(@PathVariable("name") Book book)  {
        log.info("===== getBookByName - Book: {}", book);
        return book;
    }

}

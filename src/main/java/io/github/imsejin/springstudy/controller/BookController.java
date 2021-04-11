package io.github.imsejin.springstudy.controller;

import io.github.imsejin.springstudy.editor.BookEditor;
import io.github.imsejin.springstudy.model.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("books")
public class BookController {

    /**
     * {@link java.beans.PropertyEditor}의 구현체를 지역적으로 등록한다.
     */
    @InitBinder
    public void init(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(Book.class, new BookEditor());
    }

    @GetMapping
    public List<Book> findBooks() {
        return new ArrayList<>();
    }

    @GetMapping("{book}")
    public Object findBook(@PathVariable Book book) {
        log.info("==== Book: {}", book);
        return book.getId();
    }

}

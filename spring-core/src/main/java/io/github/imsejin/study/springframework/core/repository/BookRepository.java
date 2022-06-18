package io.github.imsejin.study.springframework.core.repository;

import io.github.imsejin.study.springframework.core.model.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class BookRepository {

    public Book save(Book book) {
        log.info("Successfully saved: {}", book);
        return book;
    }

}

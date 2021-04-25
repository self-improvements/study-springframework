package io.github.imsejin.study.springframework.core.repository;

import io.github.imsejin.study.springframework.core.model.Book;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepository {

    public Book save(Book book) {
        System.out.printf("Successfully saved: %s\n", book);
        return book;
    }

}

package io.github.imsejin.springstudy.repository;

import io.github.imsejin.springstudy.model.Book;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepository {

    public Book save(Book book) {
        System.out.printf("Successfully saved: %s\n", book);
        return book;
    }

}

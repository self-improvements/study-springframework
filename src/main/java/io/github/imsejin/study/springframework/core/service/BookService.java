package io.github.imsejin.study.springframework.core.service;

import io.github.imsejin.study.springframework.core.model.Book;
import io.github.imsejin.study.springframework.core.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @PostConstruct
    public void whenConstructed() {
        System.out.printf("========== Successfully constructed: %s\n", this);
    }

    public Book publish(Book book) {
        book.setPublishedAt(LocalDate.now());
        return bookRepository.save(book);
    }

}

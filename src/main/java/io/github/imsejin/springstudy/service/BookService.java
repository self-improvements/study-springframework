package io.github.imsejin.springstudy.service;

import io.github.imsejin.springstudy.model.Book;
import io.github.imsejin.springstudy.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

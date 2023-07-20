package io.github.imsejin.study.springframework.core.service;

import io.github.imsejin.study.springframework.core.config.FullyQualifiedBeanNameGenerator;
import io.github.imsejin.study.springframework.core.model.Book;
import io.github.imsejin.study.springframework.core.repository.BookRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService implements InitializingBean {

    private final BookRepository bookRepository;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("========== afterPropertiesSet: Successfully constructed: {}", this);
    }

    @PostConstruct
    public void whenConstructed() {
        log.info("========== whenConstructed: Successfully constructed: {}", this);
    }

    public Book publish(Book book) {
        Class<FullyQualifiedBeanNameGenerator> fullyQualifiedBeanNameGeneratorClass = FullyQualifiedBeanNameGenerator.class;
        book.setPublishedAt(LocalDate.now());
        return bookRepository.save(book);
    }

}

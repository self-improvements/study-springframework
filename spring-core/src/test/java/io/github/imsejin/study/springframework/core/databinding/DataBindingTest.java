package io.github.imsejin.study.springframework.core.databinding;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.imsejin.study.springframework.core.databinding.controller.BookController;
import io.github.imsejin.study.springframework.core.databinding.controller.PeopleController;
import io.github.imsejin.study.springframework.core.databinding.converter.BookConverter;
import io.github.imsejin.study.springframework.core.databinding.formatter.UserFormatter;
import io.github.imsejin.study.springframework.core.databinding.model.Book;
import io.github.imsejin.study.springframework.core.validation.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static io.github.imsejin.study.springframework.core.databinding.formatter.UserFormatter.toId;
import static java.time.LocalDate.now;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({
        BookController.class,
        BookConverter.BookToStringConverter.class,
        BookConverter.StringToBookConverter.class,
        UserFormatter.class,
        PeopleController.class,
})
class DataBindingTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("java.beans.PropertyEditor")
    void testPropertyEditor() throws Exception {
        // given
        String id = "1";

        // when & then
        mockMvc.perform(get("/people/owner/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string(id));
    }

    @Test
    @DisplayName("org.springframework.core.convert.converter.Converter")
    void testConverter() throws Exception {
        // given
        String name = "Crime and Punishment";
        Book book = Book.builder().id(toId(name)).name(name).publishedAt(now()).build();
        String jsonContent = objectMapper.writeValueAsString(book);

        // when & then
        mockMvc.perform(get("/books/{name}", name))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonContent));
    }

    @Test
    @DisplayName("org.springframework.format.Formatter")
    void testFormatter() throws Exception {
        // given
        String name = "Im Sejin";
        User user = User.builder().id(toId(name)).name(name).build();
        String jsonContent = objectMapper.writeValueAsString(user);

        // when & then
        mockMvc.perform(get("/people/user/{name}", name))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonContent, true))
                .andDo(print());
    }

}

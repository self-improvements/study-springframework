package io.github.imsejin.study.springframework.core.databinding.controller;

import io.github.imsejin.study.springframework.core.databinding.editor.OwnerEditor;
import io.github.imsejin.study.springframework.core.databinding.model.Owner;
import io.github.imsejin.study.springframework.core.validation.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("people")
public class PeopleController {

    /**
     * {@link java.beans.PropertyEditor}의 구현체를 지역적으로 등록한다.
     */
    @InitBinder
    private void init(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(Owner.class, new OwnerEditor());
    }

    @GetMapping("owner/{id}")
    public Object getIntegerByOwnerId(@PathVariable("id") Owner owner) {
        log.info("===== getIntegerByOwnerId - Owner: {}", owner);
        return owner.getId();
    }

    @GetMapping("user/{id}")
    public Object getUserById(@PathVariable("id") User user) {
        log.info("===== getUserById - User: {}", user);
        return user;
    }

}

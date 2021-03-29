package io.github.imsejin.springstudy.profile.model;

public interface Repository<T> {

    default T save(T model) {
        System.out.printf("Successfully saved: %s\n", model);
        return model;
    }

}

package io.github.imsejin.springstudy.editor;

import io.github.imsejin.springstudy.model.Book;

import java.beans.PropertyEditorSupport;

/**
 * state를 갖고 있는 객체이기에 thread-safe하지 않음.
 * bean으로 등록했다가는 multi-thread 환경에서 낭패를 볼 수 있음.
 *
 * <p> Tomcat servlet engine 특성상 1 request가 1 thread에 매핑되는데
 * 여러 request를 받는 경우, 예상치 못한 결과값을 받을 수 있음.
 *
 * <p> thread scope한 bean으로 등록한다면 그나마 안전하나 애초에
 * bean으로 등록하지 않는 것을 권장함.
 */
public class BookEditor extends PropertyEditorSupport {

    @Override
    public String getAsText() {
        Book book = (Book) getValue(); // #2. 저장한 값을 가져온다.
        return book.getId().toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        Book book = new Book(Integer.parseInt(text));
        setValue(book); // #1. 값을 저장한다.
    }

}

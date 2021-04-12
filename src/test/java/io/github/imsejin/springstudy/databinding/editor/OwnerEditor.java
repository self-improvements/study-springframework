package io.github.imsejin.springstudy.databinding.editor;

import io.github.imsejin.springstudy.databinding.model.Owner;
import org.springframework.validation.DataBinder;

import java.beans.PropertyEditor;
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
 *
 * <p> {@link DataBinder}가 {@link PropertyEditor}들을 이용해 data binding을 진행한다.
 */
public class OwnerEditor extends PropertyEditorSupport {

    @Override
    public String getAsText() {
        Owner owner = (Owner) getValue(); // #2. 저장한 값을 가져온다.
        return owner.getId().toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        Owner owner = new Owner(Integer.parseInt(text));
        setValue(owner); // #1. 값을 저장한다.
    }

}

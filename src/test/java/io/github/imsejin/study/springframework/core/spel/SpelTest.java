package io.github.imsejin.study.springframework.core.spel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * {@code #{...}}은 expression/bean reference/template expression.
 * {@code ${...}}은 resource reference.
 */
@SpringBootTest
@RequiredArgsConstructor
class SpelTest {

    private final ExpressionParser parser = new SpelExpressionParser();

    private final ApplicationContext context;

    @Value("#{1 eq 1}")
    private boolean trueOrFalse;

    @Value("#{'Hello,' + ' World!'}")
    private String greeting;

    @Value("${app.name}")
    private String appName;

    // Resource reference in expression.
    @Value("#{'${app.name}'.replace('-', '_').toUpperCase()}")
    private String appNameUpperCase;

    @Test
    @DisplayName("org.springframework.beans.factory.annotation.Value")
    void valueAnnotation() {
        assertThat(trueOrFalse).isTrue();
        assertThat(greeting)
                .isNotBlank().isEqualTo("Hello, World!");
        assertThat(appName)
                .isNotBlank()
                .isEqualTo(context.getEnvironment().getProperty("app.name"));
        assertThat(appNameUpperCase)
                .isNotBlank().isEqualTo("STUDY_SPRINGFRAMEWORK_CORE");
    }

    @Test
    @DisplayName("Normal expression")
    void normalExpression() {
        // given
        String src = "'STUDY_SPRINGFRAMEWORK_CORE'.replace('_', '-').toLowerCase()";

        // when
        Expression expression = parser.parseExpression(src);

        // then
        assertThat(expression.getValue(String.class))
                .isEqualTo(context.getEnvironment().getProperty("app.name"));
    }

    @Test
    @DisplayName("Java bean property expression")
    void javaBeanPropertyExpression() {
        // given
        String src = "'springframework'.bytes.length";

        // when
        Expression expression = parser.parseExpression(src);

        // then
        assertThat(expression.getValue(int.class))
                .isEqualTo("springframework".getBytes().length);
    }

    @Test
    @DisplayName("Root object expression")
    void rootObjectExpression(TestInfo testInfo) {
        // given
        String src = "displayName.length";

        // when
        Expression expression = parser.parseExpression(src);

        // then
        assertThat(expression.getValue(testInfo, int.class))
                .isPositive()
                .isEqualTo(testInfo.getDisplayName().length());
    }

    @Test
    @DisplayName("Injection expression")
    void injectionExpression() {
        // given
        TestModel testModel = new TestModel();
        testModel.setName("imsejin");

        // when
//        SimpleEvaluationContext evalContext = SimpleEvaluationContext.forReadOnlyDataBinding().build();
        Expression expression = parser.parseExpression("name");
        String tobe = "Name is corrupted!";
        expression.setValue(testModel, tobe);

        // then
        assertThat(tobe)
                .isEqualTo(testModel.getName());
    }

    @Test
    @DisplayName("Failure of value injection without access")
    void setValueWithoutAccess() {
        // given
        String string = "Spring Framework";
        String src = "value";

        // when
        Expression expression = parser.parseExpression(src);

        // then
        assertThatThrownBy(() -> expression.setValue(string, "Hooray!"))
                .isExactlyInstanceOf(SpelEvaluationException.class)
                .hasMessageStartingWith("EL1010E: Property or field")
                .hasMessageEndingWith("maybe not public or not writable?");
    }

    @Test
    @DisplayName("Evaluation context expression")
    void evaluationContextExpression() {
        // given
        TestModel testModel = new TestModel();
        testModel.setName("i0m1s2e3j4i5n");

        // when
        StandardEvaluationContext evalContext = new StandardEvaluationContext(testModel);

        // "collection.?[boolean expression]" is a collection selection.
        // "#this" refers to each element in a collection.
        Expression expression = parser.parseExpression(
                "new String(name.toCharArray().?[T(Character).isDigit(#this)]).replace(',', '')");

        // then
        assertThat(expression.getValue(evalContext, String.class))
                .isEqualTo(testModel.getName().chars().mapToObj(c -> Character.toString((char) c))
                        .filter(c -> Character.isDigit(c.charAt(0)))
                        .collect(joining()));
    }

    ///////////////////////////////////////////////////////////////////////////////

    @Getter
    @Setter
    private static class TestModel {
        private long id;
        private String name;
    }

}

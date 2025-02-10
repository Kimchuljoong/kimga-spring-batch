package kr.co.kimga.batch.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionUtilsTest {

    private static class TestClass {
        private String stringField;
        private int intField;
        private static final String CONSTANT = "constant";
    }

    @Test
    void getFieldNames() {
        List<String> fieldNames = ReflectionUtils.getFieldNames(TestClass.class);

        assertThat(fieldNames).hasSize(2)
                .containsExactly("stringField", "intField")
                .doesNotContain("CONSTANT");
    }
}
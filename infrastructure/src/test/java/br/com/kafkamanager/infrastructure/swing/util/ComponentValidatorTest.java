package br.com.kafkamanager.infrastructure.swing.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.swing.JTextField;
import org.junit.jupiter.api.Test;

class ComponentValidatorTest {

    @Test
    void shouldValidateWithValidInteger() {
        JTextField integerField = new JTextField("123");
        boolean isValid = ComponentValidator.validate(integerField,
            ComponentValidator.FieldType.INTEGER);
        assertTrue(isValid);
    }

    @Test
    void shouldValidateWithInvalidInteger() {
        JTextField integerField = new JTextField("abc");
        boolean isValid = ComponentValidator.validate(integerField,
            ComponentValidator.FieldType.INTEGER);
        assertFalse(isValid);
    }

    @Test
    void shouldValidateWithValidDouble() {
        JTextField doubleField = new JTextField("3.14");
        boolean isValid = ComponentValidator.validate(doubleField,
            ComponentValidator.FieldType.DOUBLE);
        assertTrue(isValid);
    }

    @Test
    void shouldValidateWithInvalidDouble() {
        JTextField doubleField = new JTextField("abc");
        boolean isValid = ComponentValidator.validate(doubleField,
            ComponentValidator.FieldType.DOUBLE);
        assertFalse(isValid);
    }

    @Test
    void shouldValidateWithNonEmptyString() {
        JTextField stringField = new JTextField("Hello, world!");
        boolean isValid = ComponentValidator.validate(stringField,
            ComponentValidator.FieldType.STRING);
        assertTrue(isValid);
    }

    @Test
    void shouldValidateWithEmptyString() {
        JTextField stringField = new JTextField("");
        boolean isValid = ComponentValidator.validate(stringField,
            ComponentValidator.FieldType.STRING);
        assertFalse(isValid);
    }
}

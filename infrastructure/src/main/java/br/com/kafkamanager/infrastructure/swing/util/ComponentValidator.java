package br.com.kafkamanager.infrastructure.swing.util;

import javax.swing.JComponent;
import javax.swing.JTextField;

public class ComponentValidator {

    public enum FieldType {
        INTEGER, DOUBLE, DATE, STRING
    }

    public static boolean validate(JComponent component, FieldType type) {
        String value =
            component instanceof JTextField ? ((JTextField) component).getText().strip() : "";

        switch (type) {
            case INTEGER:
                try {
                    Integer.parseInt(value);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            case DOUBLE:
                try {
                    Double.parseDouble(value);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            case DATE:
                //TODO Implementação da validação de data
                return true;
            case STRING:
            default:
                return value.length() > 0;
        }
    }
}

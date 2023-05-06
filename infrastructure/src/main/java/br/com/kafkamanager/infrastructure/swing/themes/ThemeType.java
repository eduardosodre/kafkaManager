package br.com.kafkamanager.infrastructure.swing.themes;

public enum ThemeType {
    DARK("Dark"), LIGHT("Light"), INTELLIJ("IntelliJ"), DARCULA("Darcula"), MAC_DARK("Mac Dark"), MAC_LIGHT("Mac Light");

    public final String description;
    ThemeType(String description) {
        this.description = description;
    }
}

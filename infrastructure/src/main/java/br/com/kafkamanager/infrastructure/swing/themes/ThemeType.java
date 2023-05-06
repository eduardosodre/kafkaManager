package br.com.kafkamanager.infrastructure.swing.themes;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

public enum ThemeType {
    DARK("Dark"), LIGHT("Light"), INTELLIJ("IntelliJ"), DARCULA("Darcula"), MAC_DARK("Mac Dark"), MAC_LIGHT("Mac Light");

    public final String description;
    ThemeType(String description) {
        this.description = description;
    }

    public static void startTheme(String theme){
        if (ThemeType.DARK.description.equals(theme)) {
            FlatDarkLaf.setup();
        } else if (ThemeType.LIGHT.description.equals(theme)) {
            FlatLightLaf.setup();
        } else if (ThemeType.INTELLIJ.description.equals(theme)) {
            FlatIntelliJLaf.setup();
        } else if (ThemeType.DARCULA.description.equals(theme)) {
            FlatDarculaLaf.setup();
        } else if (ThemeType.MAC_DARK.description.equals(theme)) {
            FlatMacDarkLaf.setup();
        } else if (ThemeType.MAC_LIGHT.description.equals(theme)) {
            FlatMacLightLaf.setup();
        }
    }
}

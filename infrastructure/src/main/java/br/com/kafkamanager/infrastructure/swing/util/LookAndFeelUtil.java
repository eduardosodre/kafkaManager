package br.com.kafkamanager.infrastructure.swing.util;

import static com.formdev.flatlaf.FlatLaf.registerCustomDefaultsSource;

import br.com.kafkamanager.infrastructure.swing.themes.ThemeType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LookAndFeelUtil {

    public static void startLookAndFeel(String themeName) {
        try {
            registerCustomDefaultsSource("themes");
            ThemeType.startTheme(themeName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

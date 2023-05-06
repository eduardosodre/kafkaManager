package br.com.kafkamanager.infrastructure.swing.util;

import static com.formdev.flatlaf.FlatLaf.registerCustomDefaultsSource;

import com.formdev.flatlaf.FlatDarkLaf;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LookAndFeelUtil {

    public static void startLookAndFeel() {
        try {
            registerCustomDefaultsSource("themes");
            FlatDarkLaf.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

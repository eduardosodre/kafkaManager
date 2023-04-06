package br.com.kafkamanager.util;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LookAndFeelUtil {

    public static void startLookAndFeel() {
        try {
            final var lf = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(lf);
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException |
                 IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

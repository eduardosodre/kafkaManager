package br.com.kafkamanager.infrastructure.swing.util;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JOptionUtil {

    public static String showCustomInputDialog(String message, String title) {
        final var panel = new JPanel();
        final var label = new JLabel(message);
        final var textField = new JTextField(10);

        panel.add(label);
        panel.add(textField);

        int result = JOptionPane.showConfirmDialog(null, panel, title,
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            return textField.getText();
        }

        return null;
    }

    public static boolean showCustomConfirmDialog(String message, String title) {
        int dialogResult = JOptionPane.showConfirmDialog(null,
            message, title,
            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        return dialogResult == JOptionPane.YES_OPTION;
    }
}

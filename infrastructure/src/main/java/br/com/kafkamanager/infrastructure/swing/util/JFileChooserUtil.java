package br.com.kafkamanager.infrastructure.swing.util;

import br.com.kafkamanager.infrastructure.Main;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JFileChooserUtil {

    public static String chooseFile() {
        File directory = new File(Main.folder);
        if (!directory.exists()) {
            directory.mkdir();
        }
        final var fileChooser = new JFileChooser(directory);
        fileChooser.setDialogTitle("Select a file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("json file", "json"));

        final var returnVal = fileChooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.endsWith(".json")) {
                filePath += ".json";
            }
            return filePath;
        } else {
            return null;
        }
    }
}

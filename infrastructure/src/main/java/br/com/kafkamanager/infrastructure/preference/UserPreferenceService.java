package br.com.kafkamanager.infrastructure.preference;

import br.com.kafkamanager.infrastructure.util.JsonUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserPreferenceService {

    private static final String PREFERENCES_DIRECTORY_PATH = "configuration/preferences";
    private static final String PREFERENCES_FILE_PATH = PREFERENCES_DIRECTORY_PATH + "/user-preferences.json";

    public static UserPreference getPreferences() {
        createPreferencesDirectoryIfNotExists();
        try {
            return JsonUtil.readJsonFile(PREFERENCES_FILE_PATH, UserPreference.class);
        } catch (FileNotFoundException e) {
            return new UserPreference();
        }
    }

    private static void createPreferencesDirectoryIfNotExists() {
        File preferencesDirectory = new File(PREFERENCES_DIRECTORY_PATH);
        if (!preferencesDirectory.exists()) {
            preferencesDirectory.mkdirs();
        }
    }

    public static void savePreferences(UserPreference userPreference) {
        try {
            JsonUtil.writeJsonFile(userPreference, PREFERENCES_FILE_PATH);
        } catch (IOException e) {
            // handle exception
        }
    }
}

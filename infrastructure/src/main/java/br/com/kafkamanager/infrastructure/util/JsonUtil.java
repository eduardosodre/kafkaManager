package br.com.kafkamanager.infrastructure.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JsonUtil {

    public static void writeJsonFile(Object object, String filename) throws IOException {
        final var gson = new GsonBuilder().setPrettyPrinting().create();
        final var writer = new FileWriter(filename);
        gson.toJson(object, writer);
        writer.flush();
        writer.close();
    }

    public static <T> T readJsonFile(String filename, Class<T> type) throws FileNotFoundException {
        final var gson = new Gson();
        final var reader = new FileReader(filename);
        return gson.fromJson(reader, type);
    }
}

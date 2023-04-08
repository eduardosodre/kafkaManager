package br.com.kafkamanager.infrastructure.swing.util;

import java.util.HashMap;
import java.util.Map;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HeaderParser {

    public static Map<String, String> parseEventString(String eventString) {
        final var eventMap = new HashMap<String, String>();
        final var keyValuePairs = eventString.split(", ");
        for (String pair : keyValuePairs) {
            final var entry = pair.split(": ");
            final var key = entry[0];
            final var value = entry[1];
            eventMap.put(key, value);
        }
        return eventMap;
    }
}

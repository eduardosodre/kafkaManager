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


    public static String mapToString(Map<String, String> map) {
        if (map == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append("  \"");
            sb.append(entry.getKey());
            sb.append("\": \"");
            sb.append(entry.getValue());
            sb.append("\",\n");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("\n}");
        return sb.toString();
    }
}

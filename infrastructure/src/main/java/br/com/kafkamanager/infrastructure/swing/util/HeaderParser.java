package br.com.kafkamanager.infrastructure.swing.util;

import java.util.Map;
import java.util.TreeMap;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HeaderParser {

    public static Map<String, String> parseEventString(String eventString) {
        final var eventMap = new TreeMap<String, String>();
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
        if (map == null || map.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey());
            sb.append(": ");
            sb.append(entry.getValue());
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }
}

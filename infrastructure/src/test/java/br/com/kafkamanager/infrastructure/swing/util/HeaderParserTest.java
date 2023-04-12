package br.com.kafkamanager.infrastructure.swing.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class HeaderParserTest {

    @Test
    void shouldParseEventString() {
        String eventString = "eventType: Click, buttonId: 123, userId: 456";
        final var eventMap = HeaderParser.parseEventString(eventString);
        assertEquals("Click", eventMap.get("eventType"));
        assertEquals("123", eventMap.get("buttonId"));
        assertEquals("456", eventMap.get("userId"));
    }

    @Test
    void shouldMapToString() {
        final var map = new HashMap<String, String>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        String expected = "{\n" +
            "  \"key1\": \"value1\",\n" +
            "  \"key2\": \"value2\"\n" +
            "}";

        String result = HeaderParser.mapToString(map);
        assertEquals(expected, result);
    }

    @Test
    void shouldMapToStringNull() {
        Map<String, String> map = null;

        String expected = "";

        String result = HeaderParser.mapToString(map);
        assertEquals(expected, result);
    }
}

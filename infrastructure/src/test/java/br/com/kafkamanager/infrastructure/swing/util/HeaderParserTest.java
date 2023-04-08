package br.com.kafkamanager.infrastructure.swing.util;

import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HeaderParserTest {

    @Test
    void shouldParseEventString() {
        String eventString = "eventType: Click, buttonId: 123, userId: 456";
        Map<String, String> eventMap = HeaderParser.parseEventString(eventString);
        Assertions.assertEquals("Click", eventMap.get("eventType"));
        Assertions.assertEquals("123", eventMap.get("buttonId"));
        Assertions.assertEquals("456", eventMap.get("userId"));
    }
}

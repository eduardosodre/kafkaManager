package br.com.kafkamanager.domain.message;

import static br.com.kafkamanager.domain.message.MessageValidator.HEADER_KEY_INVALID_ERROR_MESSAGE;
import static br.com.kafkamanager.domain.message.MessageValidator.MESSAGE_KEY_EMPTY_ERROR_MESSAGE;
import static br.com.kafkamanager.domain.message.MessageValidator.MESSAGE_KEY_NULL_ERROR_MESSAGE;
import static br.com.kafkamanager.domain.message.MessageValidator.MESSAGE_MESSAGE_EMPTY_ERROR_MESSAGE;
import static br.com.kafkamanager.domain.message.MessageValidator.MESSAGE_MESSAGE_NULL_ERROR_MESSAGE;
import static br.com.kafkamanager.domain.message.MessageValidator.MESSAGE_TOPIC_EMPTY_ERROR_MESSAGE;
import static br.com.kafkamanager.domain.message.MessageValidator.MESSAGE_TOPIC_NULL_ERROR_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.com.kafkamanager.domain.exceptions.NotificationException;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MessageTest {

    @Test
    void shouldCreateNewMessage() {
        final var expectedKey = UUID.randomUUID().toString();
        final var expectedTopicName = "brtopic.cdc.topicname.0";
        final var expectedMessage = "Message to topic brtopic";
        final var expectedHeaders = new HashMap<String, String>();

        final var actualMessage =
            new Message(MessageID.from(expectedKey), expectedTopicName, expectedMessage,
                expectedHeaders);

        Assertions.assertNotNull(actualMessage);
        Assertions.assertNotNull(actualMessage.getId());
        Assertions.assertEquals(expectedKey, actualMessage.getId().getValue());
        Assertions.assertEquals(expectedTopicName, actualMessage.getTopicName());
        Assertions.assertEquals(expectedMessage, actualMessage.getMessage());
        Assertions.assertEquals(expectedHeaders, actualMessage.getHeaders());
    }

    private static Stream<Arguments> invalidInputForUpdateParameterPeriodEffectMethod() {
        final var headers = new HashMap<String, String>();
        headers.put("Invalid", "test");
        return Stream.of(
            Arguments.of(null, "topic", "message", new HashMap<String, String>(),
                MESSAGE_KEY_NULL_ERROR_MESSAGE),
            Arguments.of("", "topic", "message", new HashMap<String, String>(),
                MESSAGE_KEY_EMPTY_ERROR_MESSAGE),
            Arguments.of("key", null, "message", new HashMap<String, String>(),
                MESSAGE_TOPIC_NULL_ERROR_MESSAGE),
            Arguments.of("key", "", "message", new HashMap<String, String>(),
                MESSAGE_TOPIC_EMPTY_ERROR_MESSAGE),
            Arguments.of("key", "topic", null, new HashMap<String, String>(),
                MESSAGE_MESSAGE_NULL_ERROR_MESSAGE),
            Arguments.of("key", "topic", "", new HashMap<String, String>(),
                MESSAGE_MESSAGE_EMPTY_ERROR_MESSAGE),
            Arguments.of("key", "topic", "message", headers, HEADER_KEY_INVALID_ERROR_MESSAGE)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidInputForUpdateParameterPeriodEffectMethod")
    void shouldCreateNewMessageWhenAttributeIsInvalid(String key, String topic, String message,
        HashMap<String, String> headers, String messageError) {

        final var messageID = MessageID.from(key);
        final var actualException = assertThrows(NotificationException.class,
            () -> new Message(messageID, topic, message, headers));

        Assertions.assertTrue(actualException.getErrors().stream()
            .anyMatch(error -> messageError.equalsIgnoreCase(error.getMessage())));
    }
}
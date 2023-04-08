package br.com.kafkamanager.domain.topic;

import static org.junit.jupiter.api.Assertions.assertThrows;

import br.com.kafkamanager.domain.exceptions.NotificationException;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TopicTest {

    @Test
    void shouldCreateNewTopic() {
        final var expectedName = "brtopic.cdc.topicname.0";
        final var expectedPartitions = 1;
        final var expectedReplications = 1;

        final var actualTopic =
            Topic.with(expectedName, expectedPartitions, expectedReplications);

        Assertions.assertNotNull(actualTopic);
        Assertions.assertNotNull(actualTopic.getId());
        Assertions.assertEquals(expectedName, actualTopic.getId().getValue());
        Assertions.assertEquals(expectedPartitions, actualTopic.getPartitions());
        Assertions.assertEquals(expectedReplications, actualTopic.getReplications());
    }

    private static Stream<Arguments> invalidTopics() {
        return Stream.of(
            Arguments.of("br", 1, 1, TopicValidator.TOPIC_NAME_LENGTH_ERROR_MESSAGE),
            Arguments.of(null, 1, 1, TopicValidator.TOPIC_NAME_NULL_ERROR_MESSAGE),
            Arguments.of("", 1, 1, TopicValidator.TOPIC_NAME_EMPTY_ERROR_MESSAGE),
            Arguments.of("brkafka.cdc.teste.0", 0, 1,
                TopicValidator.TOPIC_PARTITION_LENGTH_ERROR_MESSAGE),
            Arguments.of("brkafka.cdc.teste.0", null, 1,
                TopicValidator.TOPIC_PARTITION_NULL_ERROR_MESSAGE),
            Arguments.of("brkafka.cdc.teste.0", 1, 0,
                TopicValidator.TOPIC_REPLICATION_LENGTH_ERROR_MESSAGE),
            Arguments.of("brkafka.cdc.teste.0", 1, null,
                TopicValidator.TOPIC_REPLICATION_NULL_ERROR_MESSAGE)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidTopics")
    void shouldNotCreateNewTopicWhenAttributeIsInvalid(String name, Integer partitions,
        Integer replications, String messageError) {

        final var actualException = assertThrows(NotificationException.class,
            () -> Topic.with(name, partitions, replications));

        Assertions.assertTrue(actualException.getErrors().stream()
            .anyMatch(error -> messageError.equalsIgnoreCase(error.getMessage())));
    }
}
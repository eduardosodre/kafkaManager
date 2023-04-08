package br.com.kafkamanager.application.topic.create;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.kafkamanager.application.UseCaseTest;
import br.com.kafkamanager.domain.exceptions.NotificationException;
import br.com.kafkamanager.domain.topic.Topic;
import br.com.kafkamanager.domain.topic.TopicGateway;
import br.com.kafkamanager.domain.topic.TopicValidator;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class CreateTopicUseCaseTest extends UseCaseTest {

    @Mock
    private TopicGateway gateway;

    @InjectMocks
    private CreateTopicUseCase useCase;

    @Test
    void shouldCreateTopic() {
        final var expectedName = "brtopic.cdc.topicname.0";
        final var expectedPartitions = 1;
        final var expectedReplications = 1;

        final var aCommand = CreateTopicCommand.of(expectedName, expectedPartitions,
            expectedReplications);
        final var topic = Topic.with(expectedName, expectedPartitions, expectedReplications);
        when(gateway.create(any()))
            .thenReturn(topic);

        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.getId());

        verify(gateway).create(argThat(aTopic ->
            Objects.nonNull(aTopic.getId())
                && Objects.equals(expectedName, aTopic.getId().getValue())
                && Objects.equals(expectedPartitions, aTopic.getPartitions())
                && Objects.equals(expectedReplications, aTopic.getReplications())
        ));
    }

    @Test
    void shouldNotCreateTopicAndReturnNotification() {
        final var expectedName = "brtopic.cdc.topicname.0";
        final var expectedPartitions = 0;
        final var expectedReplications = 1;

        final var aCommand = CreateTopicCommand.of(expectedName, expectedPartitions,
            expectedReplications);

        final var actualException = Assertions.assertThrows(NotificationException.class,
            () -> useCase.execute(aCommand));

        Assertions.assertNotNull(actualException);
        Assertions.assertTrue(actualException.getErrors().size() > 0);
        Assertions.assertEquals(TopicValidator.TOPIC_PARTITION_LENGTH_ERROR_MESSAGE,
            actualException.getErrors().get(0).getMessage());

        verify(gateway, times(0)).create(any());
    }
}
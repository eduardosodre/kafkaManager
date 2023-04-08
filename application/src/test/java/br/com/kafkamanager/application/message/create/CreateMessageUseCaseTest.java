package br.com.kafkamanager.application.message.create;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.kafkamanager.application.topic.UseCaseTest;
import br.com.kafkamanager.domain.exceptions.NotificationException;
import br.com.kafkamanager.domain.message.Message;
import br.com.kafkamanager.domain.message.MessageGateway;
import br.com.kafkamanager.domain.message.MessageValidator;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class CreateMessageUseCaseTest extends UseCaseTest {

    @Mock
    private MessageGateway gateway;

    @InjectMocks
    private CreateMessageUseCase useCase;

    @Test
    void shouldCreateMessage() {
        final var expectedKey = UUID.randomUUID().toString();
        final var expectedTopicName = "brtopic.cdc.topicname.0";
        final var expectedMessage = "Message to topic brtopic";
        final var expectedHeaders = new HashMap<String, String>();

        final var aCommand = CreateMessageCommand.of(expectedKey, expectedTopicName,
            expectedMessage, expectedHeaders);
        final var message = Message.with(expectedKey, expectedTopicName, expectedMessage,
            expectedHeaders);
        when(gateway.create(any()))
            .thenReturn(message);

        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.getId());

        verify(gateway).create(argThat(aTopic ->
            Objects.nonNull(aTopic.getId())
                && Objects.equals(expectedKey, aTopic.getId().getValue())
                && Objects.equals(expectedTopicName, aTopic.getTopicName())
                && Objects.equals(expectedMessage, aTopic.getMessage())
                && Objects.equals(expectedHeaders, aTopic.getHeaders())
        ));
    }

    @Test
    void shouldNotCreateTopicAndReturnNotification() {
        final var expectedKey = UUID.randomUUID().toString();
        final var expectedTopicName = "";
        final var expectedMessage = "Message to topic brtopic";
        final var expectedHeaders = new HashMap<String, String>();

        final var aCommand = CreateMessageCommand.of(expectedKey, expectedTopicName,
            expectedMessage, expectedHeaders);

        final var actualException = Assertions.assertThrows(NotificationException.class,
            () -> useCase.execute(aCommand));

        Assertions.assertNotNull(actualException);
        Assertions.assertTrue(actualException.getErrors().size() > 0);
        Assertions.assertEquals(MessageValidator.MESSAGE_TOPIC_EMPTY_ERROR_MESSAGE,
            actualException.getErrors().get(0).getMessage());

        verify(gateway, times(0)).create(any());
    }

}
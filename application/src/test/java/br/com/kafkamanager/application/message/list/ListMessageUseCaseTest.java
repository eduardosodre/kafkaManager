package br.com.kafkamanager.application.message.list;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.kafkamanager.application.UseCaseTest;
import br.com.kafkamanager.domain.message.Message;
import br.com.kafkamanager.domain.message.MessageFilter;
import br.com.kafkamanager.domain.message.MessageGateway;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class ListMessageUseCaseTest extends UseCaseTest {

    @Mock
    private MessageGateway gateway;

    @InjectMocks
    private ListMessageUseCase useCase;

    @Test
    void shouldListMessage() {
        final var messages = new ArrayList<Message>();
        messages.add(Message.with("key", "topic", "mensagem 1", null));
        messages.add(Message.with("key2", "topic", "mensagem 2", null));

        when(gateway.list(any(MessageFilter.class))).thenReturn(messages);

        final var filter = new MessageFilter("topic", 0, 0L, 0L);
        final var result = useCase.execute(filter);

        assertEquals(messages, result);
        verify(gateway).list(any());
    }

}
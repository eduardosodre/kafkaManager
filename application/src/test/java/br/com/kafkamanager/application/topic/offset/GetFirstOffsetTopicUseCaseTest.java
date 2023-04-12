package br.com.kafkamanager.application.topic.offset;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.kafkamanager.application.UseCaseTest;
import br.com.kafkamanager.domain.topic.Topic;
import br.com.kafkamanager.domain.topic.TopicGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class GetFirstOffsetTopicUseCaseTest extends UseCaseTest {

    @Mock
    private TopicGateway gateway;

    @InjectMocks
    private GetFirstOffsetTopicUseCase useCase;

    @Test
    void shouldGetFirstOffset() {
        final var topic = Topic.with("brteste.cmd.teste", 1, 1);
        final var expectedOffset = 10l;

        when(gateway.getFirstOffset(any(), anyInt())).thenReturn(expectedOffset);

        final var actualOutput = useCase.execute(GetOffsetCommand.of(topic, 1));

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedOffset, actualOutput);

        verify(gateway).getFirstOffset(any(), anyInt());
    }
}
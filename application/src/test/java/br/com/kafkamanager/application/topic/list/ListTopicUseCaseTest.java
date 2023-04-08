package br.com.kafkamanager.application.topic.list;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.kafkamanager.application.topic.UseCaseTest;
import br.com.kafkamanager.domain.topic.Topic;
import br.com.kafkamanager.domain.topic.TopicGateway;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class ListTopicUseCaseTest extends UseCaseTest {

    @Mock
    private TopicGateway gateway;

    @InjectMocks
    private ListTopicUseCase useCase;

    @Test
    void shouldListTopics() {
        final var topics = List.of(
            Topic.with("topic", 1, 1),
            Topic.with("topic 2", 1, 1));

        when(gateway.list()).thenReturn(topics);

        final var actualOutput = useCase.execute();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(topics.size(), actualOutput.size());

        verify(gateway).list();
    }
}
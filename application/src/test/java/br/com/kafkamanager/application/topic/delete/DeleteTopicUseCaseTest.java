package br.com.kafkamanager.application.topic.delete;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import br.com.kafkamanager.application.topic.UseCaseTest;
import br.com.kafkamanager.domain.topic.TopicGateway;
import br.com.kafkamanager.domain.topic.TopicID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

class DeleteTopicUseCaseTest extends UseCaseTest {

    @Mock
    private TopicGateway gateway;

    @InjectMocks
    private DeleteTopicUseCase useCase;

    @Test
    void shouldListTopics() {
        final var topicID = TopicID.from("topicname");

        Mockito.doNothing().when(gateway).deleteById(any());

        useCase.execute(topicID);

        verify(gateway).deleteById(topicID);
    }

}
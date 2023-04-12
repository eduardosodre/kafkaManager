package br.com.kafkamanager.infrastructure.topic;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.kafkamanager.domain.topic.Topic;
import br.com.kafkamanager.domain.topic.TopicID;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.admin.DeleteTopicsOptions;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.internals.KafkaFutureImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TopicGatewayImplTest {

    @Mock
    private KafkaAdminClient kafkaAdminClient;

    @InjectMocks
    private TopicGatewayImpl topicGateway;

    @Test
    void shouldCreateNewTopic() {
        final var topic = Topic.with("test-topic", 1, 1);
        final var newTopic = new NewTopic("test-topic", 1, (short) 1);
        when(kafkaAdminClient.createTopics(any())).thenReturn(null);

        topicGateway.create(topic);

        verify(kafkaAdminClient).createTopics(Collections.singleton(newTopic));
    }

    @Test
    void shouldDeleteById() throws InterruptedException, ExecutionException {
        final var topicName = "test-topic";
        final var topicID = new TopicID(topicName);
        final var deleteTopicsResult = mock(DeleteTopicsResult.class);

        final var completableFuture = new KafkaFutureImpl<Void>();
        completableFuture.complete(null);

        when(kafkaAdminClient.deleteTopics(eq(Collections.singleton(topicName)),
            any(DeleteTopicsOptions.class))).thenReturn(deleteTopicsResult);
        when(deleteTopicsResult.all()).thenReturn(completableFuture);

        topicGateway.deleteById(topicID);

        verify(kafkaAdminClient, times(1)).deleteTopics(eq(Collections.singleton(topicName)),
            any(DeleteTopicsOptions.class));
        verify(deleteTopicsResult, times(1)).all();
    }
}

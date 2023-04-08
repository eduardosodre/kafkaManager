package br.com.kafkamanager.infrastructure.topic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.kafkamanager.domain.topic.Topic;
import br.com.kafkamanager.domain.topic.TopicID;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.common.KafkaFuture;
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
        final var topicID = TopicID.from("topic-name");
        when(kafkaAdminClient.deleteTopics(Collections.singleton("topic-name"))).thenReturn(null);

        topicGateway.deleteById(topicID);

        verify(kafkaAdminClient).deleteTopics(Collections.singleton("topic-name"));
    }

    @Test
    void shouldList() throws InterruptedException, ExecutionException {
        final var listTopicsResult = mock(ListTopicsResult.class);
        KafkaFuture<Iterable<TopicListing>> kafkaFuture = mock(KafkaFuture.class);
        final var topicListings = new ArrayList<TopicListing>();
        topicListings.add(new TopicListing("topic1", false));
        topicListings.add(new TopicListing("topic2", false));
        topicListings.add(new TopicListing("topic3", false));
        when(kafkaFuture.get()).thenReturn(topicListings);
        when(listTopicsResult.listings()).thenAnswer(invocationOnMock -> kafkaFuture);
        when(kafkaAdminClient.listTopics()).thenReturn(listTopicsResult);

        final var expectedTopics = new ArrayList<Topic>();
        for (TopicListing topicListing : topicListings) {
            expectedTopics.add(Topic.with(topicListing.name(), 1, 1));
        }

        List<Topic> result = topicGateway.list();

        assertEquals(expectedTopics, result);
    }
}

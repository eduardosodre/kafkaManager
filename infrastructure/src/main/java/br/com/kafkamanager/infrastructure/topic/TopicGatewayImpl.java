package br.com.kafkamanager.infrastructure.topic;

import br.com.kafkamanager.domain.topic.Topic;
import br.com.kafkamanager.domain.topic.TopicGateway;
import br.com.kafkamanager.domain.topic.TopicID;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TopicGatewayImpl implements TopicGateway {

    private final KafkaAdminClient kafkaAdminClient;

    @Override
    public Topic create(Topic topic) {
        final var newTopic = new NewTopic(topic.getId().getValue(),
            topic.getPartitions(), (short) topic.getReplications().intValue());
        kafkaAdminClient.createTopics(Collections.singleton(newTopic));
        return topic;
    }

    @Override
    public void deleteById(TopicID topicID) {
        kafkaAdminClient.deleteTopics(Collections.singleton(topicID.getValue()));
    }

    @Override
    public List<Topic> list() {
        final var listTopicsResult = kafkaAdminClient.listTopics();

        final var topics = new ArrayList<Topic>();
        try {
            for (TopicListing topicListing : listTopicsResult.listings().get()) {
                topics.add(Topic.with(topicListing.name(), 1, 1));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        return topics;
    }
}

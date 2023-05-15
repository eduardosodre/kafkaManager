package br.com.kafkamanager.infrastructure.topic;

import static br.com.kafkamanager.infrastructure.MyConfig.KAFKA_CONSUMER_TOPIC_GATEWAY;

import br.com.kafkamanager.domain.topic.Topic;
import br.com.kafkamanager.domain.topic.TopicGateway;
import br.com.kafkamanager.domain.topic.TopicID;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DeleteTopicsOptions;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class TopicGatewayImpl implements TopicGateway {

    private final AdminClient kafkaAdminClient;
    private final KafkaConsumer<String, String> kafkaConsumer;

    public TopicGatewayImpl(AdminClient kafkaAdminClient,
            @Qualifier(KAFKA_CONSUMER_TOPIC_GATEWAY) KafkaConsumer<String, String> kafkaConsumer) {
        this.kafkaAdminClient = kafkaAdminClient;
        this.kafkaConsumer = kafkaConsumer;
    }

    @Override
    public Topic create(Topic topic) {
        final var newTopic = new NewTopic(topic.getId().getValue(),
            topic.getPartitions(), (short) topic.getReplications().intValue());
        kafkaAdminClient.createTopics(Collections.singleton(newTopic));
        return topic;
    }

    @Override
    public void deleteById(TopicID topicID) {
        final var deleteTopics = kafkaAdminClient.deleteTopics(
            Collections.singleton(topicID.getValue()), new DeleteTopicsOptions());
        try {
            deleteTopics.all().get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Topic> list() {
        final var listTopicsResult = kafkaAdminClient.listTopics();
        final var topicNames = new ArrayList<String>();
        try {
            for (TopicListing topicListing : listTopicsResult.listings().get()) {
                topicNames.add(topicListing.name());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        final var describeTopicsResult = kafkaAdminClient.describeTopics(topicNames);
        final var topics = new ArrayList<Topic>();
        try {
            for (Map.Entry<String, TopicDescription> entry : describeTopicsResult.all().get()
                .entrySet()) {
                final var topicName = entry.getKey();
                final var topicDescription = entry.getValue();
                final var numPartitions = topicDescription.partitions().size();
                final var numReplicas = topicDescription.partitions().get(0).replicas().size();
                topics.add(Topic.with(topicName, numPartitions, numReplicas));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        return topics;
    }

    @Override
    public Long getLastOffset(Topic topic, int partition) {
        TopicPartition topicPartition = new TopicPartition(topic.getId().getValue(), partition);
        kafkaConsumer.assign(Collections.singleton(topicPartition));
        kafkaConsumer.seekToEnd(Collections.singleton(topicPartition));
        return kafkaConsumer.position(topicPartition);
    }

    @Override
    public Long getFirstOffset(Topic topic, int partition) {
        TopicPartition topicPartition = new TopicPartition(topic.getId().getValue(), partition);
        kafkaConsumer.assign(Collections.singleton(topicPartition));
        kafkaConsumer.seekToBeginning(Collections.singleton(topicPartition));
        return kafkaConsumer.position(topicPartition);
    }
}

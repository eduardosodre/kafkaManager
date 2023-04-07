package br.com.kafkamanager.util;

import br.com.kafkamanager.dto.KafkaProducerDto;
import br.com.kafkamanager.dto.KafkaTopicDto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import lombok.experimental.UtilityClass;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

@UtilityClass
public class KafkaUtil {

    public static List<KafkaTopicDto> getTopics(String host) {
        final var list = new ArrayList<KafkaTopicDto>();

        final var props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, host);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka-topics-table");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class.getName());

        try (AdminClient client = AdminClient.create(props)) {
            final var topicsResult = client.listTopics();
            final var topicsList = topicsResult.listings().get();

            for (TopicListing topic : topicsList) {
                final var topicName = topic.name();
                int recordsCount = 0;

                final var describeTopicsResult = client.describeTopics(
                    Collections.singleton(topicName));
                final var topicDescription = describeTopicsResult.values().get(topicName).get();
                int partitionsCount = topicDescription.partitions().size();

                list.add(new KafkaTopicDto(topicName, partitionsCount, recordsCount));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.stream()
            .sorted(Comparator.comparing(KafkaTopicDto::getName))
            .collect(Collectors.toList());
    }

    public static void createTopic(String bootstrapServers, String topicName, int numPartitions,
        int replications) throws ExecutionException, InterruptedException {
        final var props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        try (AdminClient adminClient = AdminClient.create(props)) {
            NewTopic newTopic = new NewTopic(topicName, numPartitions, (short) replications);
            adminClient.createTopics(Collections.singleton(newTopic)).all().get();
        }
    }

    public static void publish(String bootstrapServers, KafkaProducerDto dto) {
        final var topicName = dto.getTopicName();
        final var key = dto.getKey();
        final var message = dto.getMessage();
        final var headers = dto.getHeaders();

        final var props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        try (final var producer = new KafkaProducer<String, String>(props)) {
            final var headerList = new ArrayList<Header>();
            if (headers != null && headers.size() > 0) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    final var headerKey = entry.getKey();
                    final var headerValue = entry.getValue();
                    headerList.add(new RecordHeader(headerKey, headerValue.getBytes()));
                }
            }

            final var producerRecord = new ProducerRecord<>(topicName, null, null, key,
                message, headerList);
            producer.send(producerRecord);

            JOptionPane.showMessageDialog(null, "Message created successfully!");
        }
    }
}

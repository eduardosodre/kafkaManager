package br.com.kafkamanager.infrastructure.message;

import static br.com.kafkamanager.infrastructure.MyConfig.KAFKA_CONSUMER_MESSAGE_GATEWAY;

import br.com.kafkamanager.domain.message.Message;
import br.com.kafkamanager.domain.message.MessageFilter;
import br.com.kafkamanager.domain.message.MessageGateway;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MessageGatewayImpl implements MessageGateway {

    private final KafkaProducer<String, String> kafkaProducer;
    private final KafkaConsumer<String, String> consumer;

    public MessageGatewayImpl(KafkaProducer<String, String> kafkaProducer,
            @Qualifier(KAFKA_CONSUMER_MESSAGE_GATEWAY) KafkaConsumer<String, String> consumer) {
        this.kafkaProducer = kafkaProducer;
        this.consumer = consumer;
    }

    @Override
    public Message create(Message message) {
        final var topicName = message.getTopicName();
        final var key = message.getId().getValue();
        final var messageContent = message.getMessage();
        final var headers = message.getHeaders();

        final var headerList = new ArrayList<Header>();
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                final var headerKey = entry.getKey();
                final var headerValue = entry.getValue();
                headerList.add(new RecordHeader(headerKey, headerValue.getBytes()));
            }
        }

        final var producerRecord = new ProducerRecord<>(topicName, null, null, key,
                messageContent, headerList);

        final RecordMetadata recordMetadata;
        try {
            recordMetadata = kafkaProducer.send(producerRecord).get();
            final var offset = recordMetadata.offset();

            message = Message.with(key, topicName, messageContent, headers, LocalDateTime.now(),
                    offset, recordMetadata.partition());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return message;
    }

    public List<Message> list(List<MessageFilter> filters) {
        final var list = new ArrayList<Message>();
        final Map<TopicPartition, MessageFilter> topicPartitions = new HashMap<>();

        for (MessageFilter filter : filters) {
            final var partition = new TopicPartition(filter.getTopicName(),
                    filter.getPartitionNumber());
            topicPartitions.put(partition, filter);
        }

        consumer.assign(topicPartitions.keySet());

        for (Map.Entry<TopicPartition, MessageFilter> entry : topicPartitions.entrySet()) {
            consumer.seek(entry.getKey(), entry.getValue().getOffset());
            long count = 0;
            long limit = entry.getValue().getOffset() + entry.getValue().getLimit();
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {

                    final var headersMap = Arrays.stream(record.headers().toArray())
                            .collect(Collectors.toMap(
                                    Header::key,
                                    header -> new String(header.value(), StandardCharsets.UTF_8)
                            ));

                    final var message = Message.with(record.key(), record.topic(),
                            record.value(),
                            headersMap,
                            LocalDateTime.ofInstant(Instant.ofEpochMilli(record.timestamp()),
                                    ZoneId.systemDefault()),
                            record.offset(),
                            record.partition());
                    list.add(message);
                    filters.stream().filter(filter -> filter.getTopicName().equals(record.topic())
                                    && filter.getPartitionNumber().equals(record.partition()))
                            .findFirst()
                            .ifPresent(filter -> filter.setOffset(record.offset() + 1));
                    count++;
                }

                if (count >= limit || records.isEmpty()) {
                    break;
                }
            }
        }
        return list;
    }
}

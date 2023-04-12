package br.com.kafkamanager.infrastructure.message;

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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageGatewayImpl implements MessageGateway {

    private final KafkaProducer<String, String> kafkaProducer;
    private final KafkaConsumer<String, String> consumer;

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
        kafkaProducer.send(producerRecord);
        return message;
    }

    public List<Message> list(MessageFilter filter) {
        final var list = new ArrayList<Message>();

        final var partition = new TopicPartition(filter.getTopicName(),
            filter.getPartitionNumber());

        final var partitions = Arrays.asList(partition);
        consumer.assign(partitions);
        consumer.seek(partition, filter.getOffset());

        long limit = filter.getOffset() + filter.getLimit();

        long count = 0;

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {

                final var headersMap = Arrays.stream(record.headers().toArray())
                    .collect(Collectors.toMap(
                        Header::key,
                        header -> new String(header.value(), StandardCharsets.UTF_8)
                    ));

                final var message = Message.with(record.key(), filter.getTopicName(),
                    record.value(),
                    headersMap,
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(record.timestamp()),
                        ZoneId.systemDefault()));
                list.add(message);
                count++;
            }

            if (count >= limit || records.isEmpty()) {
                break;
            }
        }
        return list;
    }
}

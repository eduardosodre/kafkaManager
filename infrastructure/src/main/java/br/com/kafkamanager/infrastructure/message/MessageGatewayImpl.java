package br.com.kafkamanager.infrastructure.message;

import br.com.kafkamanager.domain.message.Message;
import br.com.kafkamanager.domain.message.MessageFilter;
import br.com.kafkamanager.domain.message.MessageGateway;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

        TopicPartition partition = new TopicPartition(filter.getTopicName(),
            filter.getPartitionNumber());

        List<TopicPartition> partitions = Arrays.asList(partition);
        consumer.assign(partitions);
        consumer.seek(partition, filter.getOffset());

        long count = 0;

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                Message message = Message.with(record.key(), filter.getTopicName(), record.value(),
                    null);
                list.add(message);
                count++;
            }

            if (count >= filter.getLimit() || records.isEmpty()) {
                break;
            }
        }
        return list;
    }
}

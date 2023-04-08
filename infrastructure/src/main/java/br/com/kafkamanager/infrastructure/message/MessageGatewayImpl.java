package br.com.kafkamanager.infrastructure.message;

import br.com.kafkamanager.domain.message.Message;
import br.com.kafkamanager.domain.message.MessageGateway;
import java.util.ArrayList;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageGatewayImpl implements MessageGateway {

    private final KafkaProducer<String, String> kafkaProducer;

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
}

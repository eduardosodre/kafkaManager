package br.com.kafkamanager.infrastructure.message;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import br.com.kafkamanager.domain.message.Message;
import br.com.kafkamanager.domain.message.MessageFilter;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class MessageGatewayImplTest {

    private MessageGatewayImpl gateway;

    @Mock
    private KafkaProducer<String, String> kafkaProducer;

    @Mock
    private KafkaConsumer<String, String> kafkaConsumer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gateway = new MessageGatewayImpl(kafkaProducer, kafkaConsumer);
    }

    @Test
    void shouldCreateMessageWithHeaders() {
        final Map<String, String> headers = new HashMap<>();
        headers.put("header1", "value1");
        headers.put("header2", "value2");
        final var message = Message.with("test-key", "test-topic", "test-message", headers);

        final var recordMetadata = new RecordMetadata(new TopicPartition("test-topic", 0), 0L, 0,
            0L, 0, 0);

        when(kafkaProducer.send(any())).thenReturn(
            CompletableFuture.completedFuture(recordMetadata));

        gateway.create(message);

        verify(kafkaProducer).send(any());
        verifyNoMoreInteractions(kafkaProducer);
    }

    @Test
    void shouldCreateMessageWithoutHeaders() {
        final var message = Message.with("test-key", "test-topic", "test-message", null);
        final var recordMetadata = new RecordMetadata(new TopicPartition("test-topic", 0), 0L, 0,
            0L, 0, 0);

        when(kafkaProducer.send(any())).thenReturn(
            CompletableFuture.completedFuture(recordMetadata));

        gateway.create(message);

        verify(kafkaProducer).send(any());
        verifyNoMoreInteractions(kafkaProducer);
    }

    @Test
    void shouldListMessages() {
        //TODO testar
        final var topicName = "test-topic";
        final var partitionNumber = 0;
        final var offset = 0L;
        final var limit = 2L;
        final var filter = new MessageFilter(topicName, partitionNumber, offset, limit);

        final var record1 = new ConsumerRecord<>(topicName, partitionNumber, offset, "key1",
            "message1");
        final var record2 = new ConsumerRecord<>(topicName, partitionNumber, offset + 1, "key2",
            "message2");
        final var consumerRecords = new ConsumerRecords<>(
            Map.of(new TopicPartition(topicName, partitionNumber), List.of(record1, record2)));

        when(kafkaConsumer.poll(Duration.ofMillis(100))).thenReturn(consumerRecords);

        final var messages = gateway.list(filter);

        Assertions.assertEquals(2, messages.size());

        verify(kafkaConsumer).assign(any());
        verify(kafkaConsumer).seek(any(), anyLong());
        verify(kafkaConsumer).poll(Duration.ofMillis(100));
        verifyNoMoreInteractions(kafkaConsumer);
    }
}

package br.com.kafkamanager.infrastructure.message;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import br.com.kafkamanager.domain.message.Message;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
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

        when(kafkaProducer.send(any())).thenReturn(null);

        gateway.create(message);

        verify(kafkaProducer).send(any());
        verifyNoMoreInteractions(kafkaProducer);
    }

    @Test
    void shouldCreateMessageWithoutHeaders() {
        final var message = Message.with("test-key", "test-topic", "test-message", null);
        when(kafkaProducer.send(any())).thenReturn(null);

        gateway.create(message);

        verify(kafkaProducer).send(any());
        verifyNoMoreInteractions(kafkaProducer);
    }
}

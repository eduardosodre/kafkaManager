package br.com.kafkamanager.infrastructure;

import static org.apache.kafka.clients.admin.AdminClient.create;

import br.com.kafkamanager.application.message.create.CreateMessageUseCase;
import br.com.kafkamanager.application.topic.create.CreateTopicUseCase;
import br.com.kafkamanager.application.topic.delete.DeleteTopicUseCase;
import br.com.kafkamanager.application.topic.list.ListTopicUseCase;
import br.com.kafkamanager.domain.message.MessageGateway;
import br.com.kafkamanager.domain.topic.TopicGateway;
import br.com.kafkamanager.infrastructure.message.MessageGatewayImpl;
import br.com.kafkamanager.infrastructure.topic.TopicGatewayImpl;
import java.util.Properties;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig {

    @Value("${server}")
    private String server;

    @Bean
    public KafkaProducer<String, String> kafkaProducer() {
        final var props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return new KafkaProducer<>(props);
    }

    @Bean
    public KafkaAdminClient kafkaAdminClient() {
        final var props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        return (KafkaAdminClient) create(props);
    }

    @Bean
    public TopicGateway topicGateway() {
        return new TopicGatewayImpl(kafkaAdminClient());
    }

    @Bean
    public MessageGateway messageGateway() {
        return new MessageGatewayImpl(kafkaProducer());
    }

    @Bean
    public CreateTopicUseCase createTopicUseCase() {
        return new CreateTopicUseCase(topicGateway());
    }

    @Bean
    public ListTopicUseCase listTopicUseCase() {
        return new ListTopicUseCase(topicGateway());
    }

    @Bean
    public DeleteTopicUseCase deleteTopicUseCase() {
        return new DeleteTopicUseCase(topicGateway());
    }

    @Bean
    public CreateMessageUseCase createMessageUseCase() {
        return new CreateMessageUseCase(messageGateway());
    }

}

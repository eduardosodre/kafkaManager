package br.com.kafkamanager.infrastructure;

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
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig {

    @Value("${KAFKA_SERVER}")
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
    public KafkaConsumer kafkaConsumer() {
        final var props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return new KafkaConsumer<String, String>(props);
    }

    @Bean
    public KafkaAdminClient kafkaAdminClient() {
        final var props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        return (KafkaAdminClient) KafkaAdminClient.create(props);
    }

    @Bean
    public TopicGateway topicGateway() {
        return new TopicGatewayImpl(kafkaAdminClient());
    }

    @Bean
    public MessageGateway messageGateway() {
        return new MessageGatewayImpl(kafkaProducer(), kafkaConsumer());
    }

    @Bean
    public CreateTopicUseCase createTopicUseCase(TopicGateway topicGateway) {
        return new CreateTopicUseCase(topicGateway);
    }

    @Bean
    public ListTopicUseCase listTopicUseCase(TopicGateway topicGateway) {
        return new ListTopicUseCase(topicGateway);
    }

    @Bean
    public DeleteTopicUseCase deleteTopicUseCase(TopicGateway topicGateway) {
        return new DeleteTopicUseCase(topicGateway);
    }

    @Bean
    public CreateMessageUseCase createMessageUseCase(MessageGateway messageGateway) {
        return new CreateMessageUseCase(messageGateway);
    }

}

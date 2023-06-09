package br.com.kafkamanager.infrastructure;

import java.util.Properties;
import lombok.Getter;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"br.com.kafkamanager.application", "br.com.kafkamanager.infrastructure"})
@Getter
public class MyConfig {

    public static final String KAFKA_CONSUMER_MESSAGE_GATEWAY = "kafkaConsumerMessageGateway";
    public static final String KAFKA_CONSUMER_TOPIC_GATEWAY = "kafkaConsumerTopicGateway";

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

    @Bean(name = KAFKA_CONSUMER_MESSAGE_GATEWAY)
    public KafkaConsumer<String, String> kafkaConsumerMessageGataway() {
        final var props = buildPropertiesKafka();

        return new KafkaConsumer<>(props);
    }

    @Bean(name = KAFKA_CONSUMER_TOPIC_GATEWAY)
    public KafkaConsumer<String, String> kafkaConsumerTopicGateway() {
        final var props = buildPropertiesKafka();

        return new KafkaConsumer<>(props);
    }

    @Bean
    public AdminClient kafkaAdminClient() {
        final var props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        return AdminClient.create(props);
    }

    private Properties buildPropertiesKafka() {
        final var props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        return props;
    }

}

package br.com.kafkamanager.domain.topic;

public interface TopicGateway {

    Topic create(Topic topic);

    void deleteById(TopicID topicID);
}

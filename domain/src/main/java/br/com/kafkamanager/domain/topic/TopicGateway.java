package br.com.kafkamanager.domain.topic;

import java.util.List;

public interface TopicGateway {

    Topic create(Topic topic);

    void deleteById(TopicID topicID);

    List<Topic> list(String server);
}

package br.com.kafkamanager.application.topic.list;

import br.com.kafkamanager.application.UseCase;
import br.com.kafkamanager.domain.topic.Topic;
import br.com.kafkamanager.domain.topic.TopicGateway;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListTopicUseCase extends UseCase<String, List<Topic>> {

    private final TopicGateway topicGateway;

    @Override
    public List<Topic> execute(String server) {
        return topicGateway.list(server);
    }
}

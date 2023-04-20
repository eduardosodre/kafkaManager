package br.com.kafkamanager.application.topic.list;

import br.com.kafkamanager.application.NullaryUseCase;
import br.com.kafkamanager.domain.topic.Topic;
import br.com.kafkamanager.domain.topic.TopicGateway;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ListTopicUseCase extends NullaryUseCase<List<Topic>> {

    private final TopicGateway gateway;

    @Override
    public List<Topic> execute() {
        return gateway.list();
    }
}

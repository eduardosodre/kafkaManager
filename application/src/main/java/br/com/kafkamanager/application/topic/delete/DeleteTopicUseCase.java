package br.com.kafkamanager.application.topic.delete;

import br.com.kafkamanager.application.UnitUseCase;
import br.com.kafkamanager.domain.topic.TopicGateway;
import br.com.kafkamanager.domain.topic.TopicID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteTopicUseCase extends UnitUseCase<TopicID> {

    private final TopicGateway gateway;

    @Override
    public void execute(TopicID topicID) {
        gateway.deleteById(topicID);
    }
}

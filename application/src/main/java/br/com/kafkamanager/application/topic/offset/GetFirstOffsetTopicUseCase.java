package br.com.kafkamanager.application.topic.offset;

import br.com.kafkamanager.application.UseCase;
import br.com.kafkamanager.domain.topic.TopicGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetFirstOffsetTopicUseCase extends UseCase<GetOffsetCommand, Long> {

    private final TopicGateway gateway;

    @Override
    public Long execute(GetOffsetCommand command) {
        return gateway.getFirstOffset(command.getTopic(), command.getPartition());
    }
}

package br.com.kafkamanager.application.message.list;

import br.com.kafkamanager.application.UseCase;
import br.com.kafkamanager.domain.message.Message;
import br.com.kafkamanager.domain.message.MessageFilter;
import br.com.kafkamanager.domain.message.MessageGateway;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ListMessageUseCase extends UseCase<MessageFilter, List<Message>> {

    private final MessageGateway gateway;

    @Override
    public List<Message> execute(MessageFilter filter) {
        return gateway.list(filter);
    }
}

package br.com.kafkamanager.application.message.create;

import br.com.kafkamanager.application.UseCase;
import br.com.kafkamanager.domain.exceptions.NotificationException;
import br.com.kafkamanager.domain.message.Message;
import br.com.kafkamanager.domain.message.MessageGateway;
import br.com.kafkamanager.domain.validation.handler.Notification;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateMessageUseCase extends UseCase<CreateMessageCommand, Message> {

    private final MessageGateway gateway;

    @Override
    public Message execute(CreateMessageCommand messageCommand) {
        final var notification = Notification.create();

        final var message = notification.validate(
            () -> Message.with(messageCommand.getKey(), messageCommand.getTopicName(),
                messageCommand.getMessage(), messageCommand.getHeaders()));

        if (notification.hasError()) {
            notify(notification);
        }

        return gateway.create(message);
    }

    private void notify(Notification notification) {
        throw new NotificationException("Could not create Message", notification);
    }
}

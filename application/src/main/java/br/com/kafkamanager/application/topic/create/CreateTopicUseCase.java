package br.com.kafkamanager.application.topic.create;

import br.com.kafkamanager.application.UseCase;
import br.com.kafkamanager.domain.exceptions.NotificationException;
import br.com.kafkamanager.domain.topic.Topic;
import br.com.kafkamanager.domain.topic.TopicGateway;
import br.com.kafkamanager.domain.validation.handler.Notification;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateTopicUseCase extends UseCase<CreateTopicCommand, Topic> {

    private final TopicGateway topicGateway;

    @Override
    public Topic execute(CreateTopicCommand topicCommand) {
        final var notification = Notification.create();

        final var aMember = notification.validate(
            () -> Topic.with(topicCommand.getName(), topicCommand.getPartitions(),
                topicCommand.getReplications()));

        if (notification.hasError()) {
            notify(notification);
        }

        return topicGateway.create(aMember);
    }

    private void notify(Notification notification) {
        throw new NotificationException("Could not create Topic", notification);
    }
}

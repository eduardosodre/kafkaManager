package br.com.kafkamanager.domain.topic;

import br.com.kafkamanager.domain.Entity;
import br.com.kafkamanager.domain.exceptions.NotificationException;
import br.com.kafkamanager.domain.validation.ValidationHandler;
import br.com.kafkamanager.domain.validation.handler.Notification;
import lombok.Getter;

@Getter
public class Topic extends Entity<TopicID> {

    private final Integer partitions;
    private final Integer replications;


    public Topic(TopicID topicID, Integer partitions, Integer replications) {
        super(topicID);
        this.partitions = partitions;
        this.replications = replications;
        selfValidate();
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasError()) {
            throw new NotificationException("Failed to create a Entity Topic", notification);
        }
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new TopicValidator(this, handler).validate();
    }
}

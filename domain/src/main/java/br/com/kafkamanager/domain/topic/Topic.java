package br.com.kafkamanager.domain.topic;

import br.com.kafkamanager.domain.Entity;
import br.com.kafkamanager.domain.exceptions.NotificationException;
import br.com.kafkamanager.domain.validation.ValidationHandler;
import br.com.kafkamanager.domain.validation.handler.Notification;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false)
public class Topic extends Entity<TopicID> {

    private final Integer partitions;
    private final Integer replications;

    private Topic(TopicID topicID, Integer partitions, Integer replications) {
        super(topicID);
        this.partitions = partitions;
        this.replications = replications;
        selfValidate();
    }

    public static Topic with(String topicName, Integer partitions, Integer replications) {
        return new Topic(TopicID.from(topicName), partitions, replications);
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

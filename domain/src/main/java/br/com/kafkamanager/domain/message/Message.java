package br.com.kafkamanager.domain.message;

import br.com.kafkamanager.domain.Entity;
import br.com.kafkamanager.domain.exceptions.NotificationException;
import br.com.kafkamanager.domain.validation.ValidationHandler;
import br.com.kafkamanager.domain.validation.handler.Notification;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false)
public class Message extends Entity<MessageID> {

    private final String topicName;
    private final String message;
    private final Map<String, String> headers;
    private final LocalDateTime timestamp;

    private Message(MessageID messageID, String topicName, String message,
        Map<String, String> headers, LocalDateTime timestamp) {
        super(messageID);
        this.topicName = topicName;
        this.message = message;
        this.headers = headers;
        this.timestamp = timestamp;
        selfValidate();
    }

    public static Message with(String key, String topicName, String message,
        Map<String, String> headers) {
        return new Message(MessageID.from(key), topicName, message, headers, LocalDateTime.now());
    }

    public static Message with(String key, String topicName, String message,
        Map<String, String> headers, LocalDateTime timestamp) {
        return new Message(MessageID.from(key), topicName, message, headers, timestamp);
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasError()) {
            throw new NotificationException("Failed to create a Message", notification);
        }
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new MessageValidator(this, handler).validate();
    }
}

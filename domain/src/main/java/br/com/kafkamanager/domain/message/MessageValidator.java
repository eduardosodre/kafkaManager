package br.com.kafkamanager.domain.message;

import br.com.kafkamanager.domain.validation.Error;
import br.com.kafkamanager.domain.validation.ValidationHandler;
import br.com.kafkamanager.domain.validation.Validator;

public class MessageValidator extends Validator {

    public static final String MESSAGE_KEY_NULL_ERROR_MESSAGE = "'message key' should not be null";
    public static final String MESSAGE_KEY_EMPTY_ERROR_MESSAGE = "'message key' should not be empty";

    public static final String MESSAGE_TOPIC_NULL_ERROR_MESSAGE = "'message topic' should not be null";
    public static final String MESSAGE_TOPIC_EMPTY_ERROR_MESSAGE = "'message topic' should not be empty";

    public static final String MESSAGE_MESSAGE_NULL_ERROR_MESSAGE = "'message content' should not be null";
    public static final String MESSAGE_MESSAGE_EMPTY_ERROR_MESSAGE = "'message content' should not be empty";

    public static final String HEADER_KEY_INVALID_ERROR_MESSAGE = "Header keys should only contain letters, numbers, and underscores, and should not start with a number or contain uppercase letters";


    private final Message message;

    public MessageValidator(Message message, ValidationHandler aHandler) {
        super(aHandler);
        this.message = message;
    }

    @Override
    public void validate() {
        checkKey();
        checkTopic();
        checkMessage();
        checkHeader();
    }

    private void checkKey() {
        final var key = this.message.getId().getValue();
        validatorString(key, MESSAGE_KEY_NULL_ERROR_MESSAGE, MESSAGE_KEY_EMPTY_ERROR_MESSAGE);
    }

    private void checkTopic() {
        final var topicName = this.message.getTopicName();
        validatorString(topicName, MESSAGE_TOPIC_NULL_ERROR_MESSAGE,
            MESSAGE_TOPIC_EMPTY_ERROR_MESSAGE);
    }

    private void checkMessage() {
        final var messageCheck = this.message.getMessage();
        validatorString(messageCheck, MESSAGE_MESSAGE_NULL_ERROR_MESSAGE,
            MESSAGE_MESSAGE_EMPTY_ERROR_MESSAGE);
    }

    private void checkHeader() {
        final var headers = this.message.getHeaders();

        if (headers == null) {
            return;
        }

        headers.forEach((key, value) -> {
            if (!key.matches("^[a-z0-9_-]+$")) {
                this.validationHandler().append(new Error(HEADER_KEY_INVALID_ERROR_MESSAGE));
                return;
            }
        });
    }

}

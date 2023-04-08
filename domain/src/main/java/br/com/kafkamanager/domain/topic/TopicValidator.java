package br.com.kafkamanager.domain.topic;

import static br.com.kafkamanager.domain.validation.ValidatorUtil.MAX_STRING_LENGTH;
import static br.com.kafkamanager.domain.validation.ValidatorUtil.MIN_STRING_LENGTH;

import br.com.kafkamanager.domain.validation.Error;
import br.com.kafkamanager.domain.validation.ValidationHandler;
import br.com.kafkamanager.domain.validation.Validator;

public class TopicValidator extends Validator {

    public static final String TOPIC_NAME_NULL_ERROR_MESSAGE = "'topic name' should not be null";
    public static final String TOPIC_NAME_EMPTY_ERROR_MESSAGE = "'topic name' should not be empty";
    public static final String TOPIC_NAME_LENGTH_ERROR_MESSAGE = "'topic name' must be between 3 and 255 characters";
    public static final String TOPIC_PARTITION_NULL_ERROR_MESSAGE = "'topic partitions' should not be null";
    public static final String TOPIC_PARTITION_LENGTH_ERROR_MESSAGE = "'topic partitions' must be between 1 and 10";
    public static final String TOPIC_REPLICATION_NULL_ERROR_MESSAGE = "'topic replications' should not be null";
    public static final String TOPIC_REPLICATION_LENGTH_ERROR_MESSAGE = "'topic replications' must be between 1 and 10";

    private final Topic topic;

    public TopicValidator(Topic topic, ValidationHandler aHandler) {
        super(aHandler);
        this.topic = topic;
    }

    @Override
    public void validate() {
        checkTopicName();
        checkTopicPartitions();
        checkTopicReplications();
    }

    private void checkTopicName() {
        final var name = topic.getId().getValue();
        validatorString(name, TOPIC_NAME_NULL_ERROR_MESSAGE, TOPIC_NAME_EMPTY_ERROR_MESSAGE);

        if (this.validationHandler().hasError()) {
            return;
        }

        final int length = name.trim().length();
        if (length > MAX_STRING_LENGTH || length < MIN_STRING_LENGTH) {
            this.validationHandler()
                .append(new Error(TOPIC_NAME_LENGTH_ERROR_MESSAGE));
        }
    }

    private void checkTopicPartitions() {
        validatorInteger(this.topic.getPartitions(), TOPIC_PARTITION_NULL_ERROR_MESSAGE,
            TOPIC_PARTITION_LENGTH_ERROR_MESSAGE);
    }

    private void checkTopicReplications() {
        validatorInteger(this.topic.getReplications(), TOPIC_REPLICATION_NULL_ERROR_MESSAGE,
            TOPIC_REPLICATION_LENGTH_ERROR_MESSAGE);
    }
}

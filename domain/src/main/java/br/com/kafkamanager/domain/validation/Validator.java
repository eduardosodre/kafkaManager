package br.com.kafkamanager.domain.validation;

public abstract class Validator {

    private final ValidationHandler handler;

    protected Validator(final ValidationHandler aHandler) {
        this.handler = aHandler;
    }

    public abstract void validate();

    protected ValidationHandler validationHandler() {
        return this.handler;
    }

    protected void validatorString(String string, String messageKeyNullErrorMessage,
        String messageKeyEmptyErrorMessage) {
        if (string == null) {
            this.validationHandler().append(new Error(messageKeyNullErrorMessage));
            return;
        }

        if (string.isBlank()) {
            this.validationHandler().append(new Error(messageKeyEmptyErrorMessage));
            return;
        }
    }

    protected void validatorInteger(Integer integer, String topicPartitionNullErrorMessage,
        String topicPartitionLengthErrorMessage) {
        if (integer == null) {
            this.validationHandler().append(new Error(topicPartitionNullErrorMessage));
            return;
        }

        if (integer > 10 || integer < 1) {
            this.validationHandler()
                .append(new Error(topicPartitionLengthErrorMessage));
        }
    }
}

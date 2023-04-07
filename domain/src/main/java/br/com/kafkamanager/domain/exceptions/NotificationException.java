package br.com.kafkamanager.domain.exceptions;

import br.com.kafkamanager.domain.validation.handler.Notification;

public class NotificationException extends DomainException {

    public NotificationException(final String aMessage, final Notification notification) {
        super(aMessage, notification.getErrors());
    }
}

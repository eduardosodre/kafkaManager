package br.com.kafkamanager.domain.message;

public interface MessageGateway {

    Message create(Message topic);
    
}

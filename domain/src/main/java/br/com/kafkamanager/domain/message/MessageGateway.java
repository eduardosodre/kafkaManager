package br.com.kafkamanager.domain.message;

import java.util.List;

public interface MessageGateway {

    Message create(Message topic);

    List<Message> list(MessageFilter filter);

}

package br.com.kafkamanager.infrastructure.message;

import br.com.kafkamanager.domain.message.Message;
import br.com.kafkamanager.domain.message.MessageFilter;
import br.com.kafkamanager.domain.topic.Topic;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumerDto {
    private String id = String.valueOf(System.currentTimeMillis());
    private Topic topic;
    private List<MessageFilter> messageFilters;
    private List<Message> listMessages = new ArrayList<>();
    private List<Message> listNewMessages = new ArrayList<>();
    private Instant startTime = Instant.now();
    private boolean running = false;

    public ConsumerDto(Topic topic, List<MessageFilter> messageFilters) {
        this.topic = topic;
        this.messageFilters = messageFilters;
    }

    public void start() {
        startTime = Instant.now();
        running = true;
    }

    public void stop() {
        running = false;
    }

    public ConsumerDto withCleanListNewMessages() {
        listNewMessages = new ArrayList<>();
        return this;
    }
}
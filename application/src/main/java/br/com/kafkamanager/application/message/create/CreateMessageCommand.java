package br.com.kafkamanager.application.message.create;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class CreateMessageCommand {

    private final String key;
    private final String topicName;
    private final String message;
    private final Map<String, String> headers;
}

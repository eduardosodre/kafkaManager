package br.com.kafkamanager.application.topic.offset;

import br.com.kafkamanager.domain.topic.Topic;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class GetOffsetCommand {

    private Topic topic;
    private Integer partition;
}
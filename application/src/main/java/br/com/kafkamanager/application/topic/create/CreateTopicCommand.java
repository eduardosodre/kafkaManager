package br.com.kafkamanager.application.topic.create;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class CreateTopicCommand {

    private final String name;
    private final Integer partitions;
    private final Integer replications;
}

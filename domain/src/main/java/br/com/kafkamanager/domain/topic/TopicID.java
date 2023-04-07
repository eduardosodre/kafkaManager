package br.com.kafkamanager.domain.topic;

import br.com.kafkamanager.domain.Identifier;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class TopicID extends Identifier {

    private final String value;

    public static TopicID from(final String anId) {
        return new TopicID(anId);
    }
}

package br.com.kafkamanager.domain.message;

import br.com.kafkamanager.domain.Identifier;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class MessageID extends Identifier {

    private final String value;

    public static MessageID from(final String value) {
        return new MessageID(value);
    }
}

package br.com.kafkamanager.domain.message;

import br.com.kafkamanager.domain.ValueObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MessageFilter extends ValueObject {

    private String topicName;
    private Integer partitionNumber;
    private Long offset;
    private Long limit;
}

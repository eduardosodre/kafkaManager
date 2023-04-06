package br.com.kafkamanager.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaProducerDto {

    private String topicName;
    private String key;
    private String message;
    private Map<String, String> headers;
}

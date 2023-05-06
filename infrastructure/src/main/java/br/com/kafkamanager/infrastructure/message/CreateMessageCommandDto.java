package br.com.kafkamanager.infrastructure.message;

import br.com.kafkamanager.application.message.create.CreateMessageCommand;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@AllArgsConstructor(staticName = "of")
@ToString(exclude = {"sentProducers", "file"})
public class CreateMessageCommandDto {
	
	@Expose
	@Setter
	private String description;
	@Expose
    private String key;
    @Expose
    private String topicName;
    @Expose
    private String message;
    @Expose
    private Map<String, String> headers;
    private List<String> sentProducers;
    @Setter
    private String file;

    
    public void update(String description, String key, String topicName, String message, Map<String, String> headers, List<String> sentProducers) {
    	this.description = description;
    	this.key = key;
    	this.topicName = topicName;
    	this.message = message;
    	this.headers = headers;
    	this.sentProducers = sentProducers;
    }
    
	public String getDescription() {
		description = Optional.ofNullable(description).orElse("Description");
		return description;
	}
	
	public List<String> getSentProducers() {
		sentProducers = Optional.ofNullable(sentProducers).orElse(new ArrayList<>());
		return sentProducers;
	}
	    
    public CreateMessageCommand buildCreateMessageCommand() {
    	return CreateMessageCommand.of(key, topicName, message, headers);
    }
}

package br.com.kafkamanager.swing;

import br.com.kafkamanager.util.ComponentValidator;
import br.com.kafkamanager.util.ComponentValidator.FieldType;
import br.com.kafkamanager.util.KafkaUtil;
import java.awt.Window;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JOptionPane;

public class KafkaCreateTopicController extends KafkaCreateTopic {

    private static final String COULD_NOT_CREATED_TOPIC_MESSAGE = "Could not create the topic";
    private static final String TOPIC_CREATED_SUCCESSFULLY_MESSAGE = "A Topic created successfully!";
    private static final String INVALID_FIELD_MESSAGE = "One or more fields are invalid. Please check the values and try again.";

    private final String host;

    public KafkaCreateTopicController(Window owner, String host) {
        super(owner);
        this.host = host;
        start();
        setVisible(true);
    }

    private void start() {
        initializeListeners();
    }

    private void initializeListeners() {
        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> dispose());
    }

    private void save() {
        final var topicName = txtTopicName.getText().strip();
        final int partitions = Integer.parseInt(txtPartitions.getText().strip());
        final int replications = Integer.parseInt(txtReplications.getText().strip());

        boolean validate = List.of(ComponentValidator.validate(txtTopicName, FieldType.STRING),
                ComponentValidator.validate(txtTopicName, FieldType.INTEGER),
                ComponentValidator.validate(txtTopicName, FieldType.INTEGER))
            .contains(false);

        if (!validate) {
            JOptionPane.showMessageDialog(this, INVALID_FIELD_MESSAGE);
            return;
        }
        try {
            KafkaUtil.createTopic(host, topicName, partitions, replications);
            JOptionPane.showMessageDialog(this, TOPIC_CREATED_SUCCESSFULLY_MESSAGE);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, COULD_NOT_CREATED_TOPIC_MESSAGE);
        }
    }

}

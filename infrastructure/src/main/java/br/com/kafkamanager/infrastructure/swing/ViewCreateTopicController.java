package br.com.kafkamanager.infrastructure.swing;

import br.com.kafkamanager.application.topic.create.CreateTopicCommand;
import br.com.kafkamanager.application.topic.create.CreateTopicUseCase;
import br.com.kafkamanager.infrastructure.swing.util.ComponentValidator;
import br.com.kafkamanager.infrastructure.swing.util.ComponentValidator.FieldType;
import br.com.kafkamanager.infrastructure.util.ContextUtil;
import java.awt.Window;
import java.util.List;
import javax.swing.JOptionPane;

public class ViewCreateTopicController extends ViewCreateTopic {

    private static final String COULD_NOT_CREATED_TOPIC_MESSAGE = "Could not create the topic";
    private static final String TOPIC_CREATED_SUCCESSFULLY_MESSAGE = "A Topic created successfully!";
    private static final String INVALID_FIELD_MESSAGE = "One or more fields are invalid. Please check the values and try again.";

    private CreateTopicUseCase createTopicUseCase;

    public ViewCreateTopicController(Window owner) {
        super(owner);
        createTopicUseCase = ContextUtil.getBean(CreateTopicUseCase.class);
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
            createTopicUseCase.execute(CreateTopicCommand.of(topicName, partitions, replications));
            JOptionPane.showMessageDialog(this, TOPIC_CREATED_SUCCESSFULLY_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, COULD_NOT_CREATED_TOPIC_MESSAGE);
        }
    }

}

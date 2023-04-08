package br.com.kafkamanager.infrastructure.swing;

import br.com.kafkamanager.application.topic.list.ListTopicUseCase;
import br.com.kafkamanager.domain.topic.Topic;
import br.com.kafkamanager.infrastructure.util.ContextUtil;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ViewDashboardController extends ViewDashboard {

    private static final String[] COLUMN_NAMES = {"Name", "Partitions", "Count"};
    private static final String NO_TOPIC_SELECTED_MESSAGE = "A topic must be selected in the table.";
    private ListTopicUseCase listTopicUseCase;
    private List<Topic> listTopics;
    private DefaultTableModel model;


    public ViewDashboardController() {
        super();
        listTopicUseCase = ContextUtil.getBean(ListTopicUseCase.class);
        start();
        setVisible(true);
    }

    private void start() {
        setupButtons();
        setupSearch();
        showTopics();
    }

    private void setupButtons() {
        btnClose.addActionListener(e -> dispose());
        btnConsumer.addActionListener(e -> consumer());
        btnCreateTopic.addActionListener(e -> createTopic());
        btnProducer.addActionListener(e -> producer());
    }

    private void setupSearch() {
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterTopics();
            }
        });
    }

    private void filterTopics() {
        final var filter = txtSearch.getText().toLowerCase();
        final var filteredTopics = listTopics.stream()
            .filter(topic -> topic.getId().getValue().toLowerCase().contains(filter))
            .collect(Collectors.toList());
        createTable();
        populateTopicTable(filteredTopics);
    }

    private void showTopics() {
        createTable();
        listTopics = listTopicUseCase.execute();
        populateTopicTable(listTopics);
    }

    private void createTable() {
        model = new DefaultTableModel(COLUMN_NAMES, 0);
        table.setModel(model);
    }

    private void populateTopicTable(List<Topic> listKafka) {
        createTable();
        listKafka.forEach(kafkaTopicDto -> model.addRow(
            new Object[]{kafkaTopicDto.getId().getValue(), kafkaTopicDto.getPartitions().toString(),
                kafkaTopicDto.getReplications().toString()}));
    }

    private void consumer() {
        //TODO create new consumer
    }

    private void producer() {
        final var selected = table.getSelectedRow();
        if (selected < 0) {
            JOptionPane.showMessageDialog(this, NO_TOPIC_SELECTED_MESSAGE);
            return;
        }
        final var topicName = table.getValueAt(selected, 0).toString();
        new ViewProducerController(this, topicName);
    }

    private void createTopic() {
        new ViewCreateTopicController(this);
        showTopics();
    }
}

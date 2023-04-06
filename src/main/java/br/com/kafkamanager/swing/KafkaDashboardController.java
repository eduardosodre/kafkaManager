package br.com.kafkamanager.swing;

import br.com.kafkamanager.dto.KafkaTopicDto;
import br.com.kafkamanager.util.KafkaUtil;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class KafkaDashboardController extends KafkaDashboard {

    private static final String[] COLUMN_NAMES = {"Name", "Partitions", "Count"};
    private static final String COULD_NOT_CONNECT_MESSAGE = "Could not connect to the server.";
    private static final String NO_TOPIC_SELECTED_MESSAGE = "A topic must be selected in the table.";

    private final String host;
    private List<KafkaTopicDto> listTopics;
    private DefaultTableModel model;

    public KafkaDashboardController() {
        super();
        host = new KafkaConfigController(this).getHost();
        start();
        setVisible(true);
    }

    private void start() {
        if (host == null) {
            JOptionPane.showMessageDialog(this, COULD_NOT_CONNECT_MESSAGE);
        }
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
        final var filteredTopics = this.listTopics.stream()
            .filter(topic -> topic.getName().toLowerCase().contains(filter))
            .distinct()
            .collect(Collectors.toList());
        createTable();
        populateTopicTable(filteredTopics);
    }

    private void showTopics() {
        createTable();
        listTopics = KafkaUtil.getTopics(host);
        populateTopicTable(listTopics);
    }

    private void createTable() {
        model = new DefaultTableModel(COLUMN_NAMES, 0);
        table.setModel(model);
    }

    private void populateTopicTable(List<KafkaTopicDto> listKafka) {
        createTable();
        listKafka.forEach(kafkaTopicDto -> model.addRow(
            new Object[]{kafkaTopicDto.getName(), kafkaTopicDto.getPartitions().toString(),
                kafkaTopicDto.getRecords().toString()}));
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
        new KafkaProducerController(this, host, topicName);
    }

    private void createTopic() {
        //TODO create new topic
    }
}

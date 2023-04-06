package br.com.kafkamanager.swing;

import br.com.kafkamanager.dto.KafkaProducerDto;
import br.com.kafkamanager.util.HeaderParser;
import br.com.kafkamanager.util.JFileChooserUtil;
import br.com.kafkamanager.util.JOptionUtil;
import br.com.kafkamanager.util.JsonUtil;
import br.com.kafkamanager.util.KafkaUtil;
import java.awt.Window;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class KafkaProducerController extends KafkaProducer {

    private static final String[] COLUMN_NAMES = {"Key", "Value"};
    private static final String COULD_NOT_LOAD_FILE_MESSAGE = "Could not load the file";
    private static final String FILE_SAVED_SUCCESSFULLY_MESSAGE = "A File saved successfully!";
    private static final String COULD_NOT_SAVE_FILE_MESSAGE = "Could not save the file!";

    private final String host;
    private final String topicName;
    private DefaultTableModel model;

    public KafkaProducerController(Window owner, String host, String topicName) {
        super(owner);
        this.topicName = topicName;
        this.host = host;
        start();
        setVisible(true);
    }

    private void start() {
        initializeTopics();
        initializeListeners();
        createTable();
        model.addRow(new Object[]{"", ""});
    }

    private void initializeTopics() {
        final var topics = KafkaUtil.getTopics(host);
        topics.forEach(kafkaTopicDto -> comboTopic.addItem(kafkaTopicDto.getName()));
        comboTopic.setSelectedItem(topicName);
    }

    private void initializeListeners() {
        btnClose.addActionListener(e -> dispose());
        btnProduce.addActionListener(e -> producer());
        btnLoadProducer.addActionListener(e -> load());
        btnSave.addActionListener(e -> save());
        btnPlus.addActionListener(e -> plus());
        btnSubtract.addActionListener(e -> subtract());
        btnImportHeaderKafdrop.addActionListener(e -> importHeaderKafrop());
    }

    private void createTable() {
        model = new DefaultTableModel(COLUMN_NAMES, 0);
        table.setModel(model);
    }

    private void producer() {
        KafkaUtil.publish(host, getKafkaProducerDto());
    }

    private void load() {
        final var file = JFileChooserUtil.chooseFile();
        if (file == null) {
            return;
        }
        try {
            final var kafkaProducerDto = JsonUtil.readJsonFile(file, KafkaProducerDto.class);
            comboTopic.setSelectedItem(kafkaProducerDto.getTopicName());
            txtKey.setText(kafkaProducerDto.getKey());
            txtValue.setText(kafkaProducerDto.getMessage());
            populateTableWithHeaders(kafkaProducerDto.getHeaders());
        } catch (FileNotFoundException e) {
            showMessageDialog(COULD_NOT_LOAD_FILE_MESSAGE);
        }
    }

    private void save() {
        final var file = JFileChooserUtil.chooseFile();
        if (file == null) {
            return;
        }
        try {
            JsonUtil.writeJsonFile(getKafkaProducerDto(), file);
            showMessageDialog(FILE_SAVED_SUCCESSFULLY_MESSAGE);
        } catch (IOException e) {
            showMessageDialog(COULD_NOT_SAVE_FILE_MESSAGE);
        }
    }

    private void plus() {
        model.addRow(new Object[]{"", ""});
    }

    private void subtract() {
        model.removeRow(table.getSelectedRow());
    }

    private KafkaProducerDto getKafkaProducerDto() {
        final var map = new HashMap<String, String>();
        final var totalRow = table.getRowCount();
        for (int i = 0; i < totalRow; i++) {
            String key = table.getValueAt(i, 0).toString().strip();
            String value = table.getValueAt(i, 1).toString().strip();
            if (!value.isBlank()) {
                map.put(key, value);
            }
        }
        return KafkaProducerDto.builder()
            .key(txtKey.getText().strip())
            .message(txtValue.getText().strip())
            .headers(map)
            .topicName(String.valueOf(comboTopic.getSelectedItem()))
            .build();
    }

    private void importHeaderKafrop() {
        final var header = JOptionUtil.showCustomInputDialog("Copy the header from Kafdrop:",
            "Complete");
        if (header == null || header.isBlank()) {
            return;
        }
        populateTableWithHeaders(HeaderParser.parseEventString(header));
    }

    private void populateTableWithHeaders(Map<String, String> headerMap) {
        createTable();
        headerMap.forEach((key, value) -> model.addRow(new Object[]{key, value}));
    }

    private void showMessageDialog(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}

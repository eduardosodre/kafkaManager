package br.com.kafkamanager.infrastructure.swing;

import br.com.kafkamanager.application.message.list.ListMessageUseCase;
import br.com.kafkamanager.application.topic.offset.GetFirstOffsetTopicUseCase;
import br.com.kafkamanager.application.topic.offset.GetLastOffsetTopicUseCase;
import br.com.kafkamanager.application.topic.offset.GetOffsetCommand;
import br.com.kafkamanager.domain.message.Message;
import br.com.kafkamanager.domain.message.MessageFilter;
import br.com.kafkamanager.domain.topic.Topic;
import br.com.kafkamanager.infrastructure.swing.util.HeaderParser;
import br.com.kafkamanager.infrastructure.util.ContextUtil;
import br.com.kafkamanager.infrastructure.util.JsonUtil;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ViewConsumerController extends ViewConsumer {

    private static final String[] COLUMN_NAMES = {"Offset", "Key", "Timestamp", "Message",
        "Headers"};
    private final Topic topic;
    private final ListMessageUseCase listMessageUseCase;
    private final GetLastOffsetTopicUseCase getLastOffsetTopicUseCase;
    private final GetFirstOffsetTopicUseCase getFirstOffsetTopicUseCase;
    private DefaultTableModel model;

    public ViewConsumerController(Topic topic) {
        super();
        this.topic = topic;
        listMessageUseCase = ContextUtil.getBean(ListMessageUseCase.class);
        getLastOffsetTopicUseCase = ContextUtil.getBean(GetLastOffsetTopicUseCase.class);
        getFirstOffsetTopicUseCase = ContextUtil.getBean(GetFirstOffsetTopicUseCase.class);
        setTitle("Consumer " + topic.getId().getValue());
        start();
        setVisible(true);
    }

    private void start() {
        initializeListeners();
        populatePartitions();
        createTable();
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent e) {
                txtPartition.setSelectedIndex(0);
                showOffset();
                listMessage();
            }
        });
    }

    private void showOffset() {
        final var offsetCommand = GetOffsetCommand.of(topic, txtPartition.getSelectedIndex());
        final var firstOffset = getFirstOffsetTopicUseCase.execute(offsetCommand);
        final var offsetString = String.format("First Offset: %d - Last Offset: %d",
            firstOffset,
            getLastOffsetTopicUseCase.execute(offsetCommand));
        lbOffset.setText(offsetString);
        txtOffset.setText(String.valueOf(firstOffset));
    }

    private void populatePartitions() {
        for (int i = 0; i < topic.getPartitions(); i++) {
            txtPartition.addItem(String.valueOf(i));
        }
        txtPartition.setSelectedIndex(0);
    }

    private void initializeListeners() {
        btnClose.addActionListener(e -> dispose());
        txtPartition.addActionListener(e -> {
            listMessage();
            showOffset();
        });
        txtOffset.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    listMessage();
                    showOffset();
                }
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    final var headers = table.getValueAt(selectedRow, 4).toString();
                    final var message = table.getValueAt(selectedRow, 3).toString();
                    txtHeaders.setText(headers);
                    txtMessage.setText(JsonUtil.format(message));

                    txtMessage.setCaretPosition(1);
                }
            }
        });
    }

    private void createTable() {
        model = new DefaultTableModel(COLUMN_NAMES, 0);
        table.setModel(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        table.getColumnModel().getColumn(0).setPreferredWidth(55);
        table.getColumnModel().getColumn(1).setPreferredWidth(260);
        table.getColumnModel().getColumn(2).setPreferredWidth(160);
        table.getColumnModel().getColumn(3).setPreferredWidth(450);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
    }

    public void listMessage() {
        final var topicName = topic.getId().getValue();
        final var partition = txtPartition.getSelectedIndex();
        final var offset = Long.valueOf(txtOffset.getText());
        final var limit = 100L;
        final var messageFilter = new MessageFilter(topicName, partition, offset, limit);
        final var listMessage = listMessageUseCase.execute(messageFilter);
        populateMessageTable(listMessage);
    }

    private void populateMessageTable(List<Message> listMessages) {
        createTable();
        listMessages.forEach(message -> model.addRow(
            new Object[]{message.getOffset(), message.getId().getValue(), message.getTimestamp(),
                message.getMessage(), HeaderParser.mapToString(message.getHeaders())}));
    }
}

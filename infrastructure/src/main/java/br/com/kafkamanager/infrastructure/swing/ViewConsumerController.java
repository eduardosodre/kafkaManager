package br.com.kafkamanager.infrastructure.swing;

import br.com.kafkamanager.application.message.list.ListMessageUseCase;
import br.com.kafkamanager.domain.message.Message;
import br.com.kafkamanager.domain.message.MessageFilter;
import br.com.kafkamanager.domain.topic.Topic;
import br.com.kafkamanager.infrastructure.util.ContextUtil;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

public class ViewConsumerController extends ViewConsumer {

    private static final String[] COLUMN_NAMES = {"Key", "Headers", "Message"};
    private final Topic topic;
    private ListMessageUseCase listMessageUseCase;
    private DefaultTableModel model;

    public ViewConsumerController(Window owner, Topic topic) {
        super(owner);
        this.topic = topic;
        listMessageUseCase = ContextUtil.getBean(ListMessageUseCase.class);
        start();
        setVisible(true);
    }

    private void start() {
        initializeListeners();
        populatePartitions();
        createTable();
    }

    private void populatePartitions() {
        for (int i = 0; i < topic.getPartitions(); i++) {
            txtPartition.addItem(String.valueOf(i));
        }
        txtPartition.setSelectedIndex(0);
    }

    private void initializeListeners() {
        btnClose.addActionListener(e -> dispose());
        txtPartition.addActionListener(e -> listMessage());
        txtOffset.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    listMessage();
                }
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    final var headers = table.getValueAt(selectedRow, 1).toString();
                    final var message = table.getValueAt(selectedRow, 2).toString();
                    lbHeaders.setText(headers);
                    lbMessage.setText(message);
                }
            }
        });

        copyClipboard(lbMessage);
        copyClipboard(lbHeaders);
    }

    private void copyClipboard(JLabel label) {
        label.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    StringSelection stringSelection = new StringSelection(label.getText());
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(stringSelection, null);
                }
            }
        });
    }

    private void createTable() {
        model = new DefaultTableModel(COLUMN_NAMES, 0);
        table.setModel(model);
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
            new Object[]{message.getId().getValue(), message.getHeaders().toString(),
                message.getMessage()}));
    }
}

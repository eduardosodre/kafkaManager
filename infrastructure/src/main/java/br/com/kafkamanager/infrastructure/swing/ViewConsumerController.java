package br.com.kafkamanager.infrastructure.swing;

import static java.lang.Boolean.FALSE;

import br.com.kafkamanager.application.message.list.ListMessageUseCase;
import br.com.kafkamanager.application.topic.list.ListTopicUseCase;
import br.com.kafkamanager.application.topic.offset.GetFirstOffsetTopicUseCase;
import br.com.kafkamanager.application.topic.offset.GetLastOffsetTopicUseCase;
import br.com.kafkamanager.application.topic.offset.GetOffsetCommand;
import br.com.kafkamanager.domain.message.Message;
import br.com.kafkamanager.domain.message.MessageFilter;
import br.com.kafkamanager.domain.topic.Topic;
import br.com.kafkamanager.domain.topic.TopicID;
import br.com.kafkamanager.infrastructure.message.ConsumerDto;
import br.com.kafkamanager.infrastructure.swing.util.HeaderParser;
import br.com.kafkamanager.infrastructure.swing.util.MonochromeTableCellRenderer;
import br.com.kafkamanager.infrastructure.swing.util.MyTableModel;
import br.com.kafkamanager.infrastructure.swing.util.SetupColor;
import br.com.kafkamanager.infrastructure.util.ContextUtil;
import br.com.kafkamanager.infrastructure.util.JsonUtil;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ViewConsumerController extends ViewConsumer {

    private static final String[] COLUMN_NAMES = {"Offset", "Key", "Timestamp", "Message",
            "Headers", "Partition"};
    private static final String CANNOT_REMOVE_CONSUMER_BECAUSE_IS_RUNNING = "Cannot remove Consumer because it is running";
    public static final String ITEM_ALL_PARTITIONS = "All";
    public static final String CANNOT_RUN_BECAUSE_NO_CONSUMER_HAS_BEEN_SELECTED = "Cannot run because no Consumer has been selected";
    private final ListMessageUseCase listMessageUseCase;
    private final ListTopicUseCase listTopicUseCase;
    private final GetLastOffsetTopicUseCase getLastOffsetTopicUseCase;
    private final GetFirstOffsetTopicUseCase getFirstOffsetTopicUseCase;
    private DefaultTableModel model;
    private JTextField txtComboTopic;
    private MyTableModel<ConsumerDto> modelListConsumer;
    private boolean isShowingConsumer = false;
    private ScheduledExecutorService schedulerRuntime;
    private Thread threadMessage;
    private boolean schedulerMessage = true;
    private Topic topicSelected;

    public ViewConsumerController() {
        super();
        listMessageUseCase = ContextUtil.getBean(ListMessageUseCase.class);
        listTopicUseCase = ContextUtil.getBean(ListTopicUseCase.class);
        getLastOffsetTopicUseCase = ContextUtil.getBean(GetLastOffsetTopicUseCase.class);
        getFirstOffsetTopicUseCase = ContextUtil.getBean(GetFirstOffsetTopicUseCase.class);
        start();
        setVisible(true);
    }

    private void start() {
        initializeTopics();
        setupIcons();
        initializeListeners();
        createTable();
        createTableListConsumer();
    }

    private void initializeTopics() {
        final var topics = new TreeSet<>(listTopicUseCase.execute());
        topics.stream().map(Topic::getId)
                .map(TopicID::getValue)
                .forEach(comboTopic::addItem);
    }

    private void showOffset(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED && txtPartition.getSelectedIndex() > 0) {
            panelOffset.setVisible(true);
            getTopic().ifPresent(topic -> {
                final var offsetCommand = GetOffsetCommand.of(topic,
                        txtPartition.getSelectedIndex() - 1);
                final var firstOffset = getFirstOffsetTopicUseCase.execute(offsetCommand);
                final var offsetString = String.format("First Offset: %d - Last Offset: %d",
                        firstOffset,
                        getLastOffsetTopicUseCase.execute(offsetCommand));
                lbOffset.setText(offsetString);
                txtOffset.setText(String.valueOf(firstOffset));
            });

        } else {
            panelOffset.setVisible(false);
        }
    }

    private void populatePartitions() {
        txtPartition.removeAllItems();
        txtPartition.addItem(ITEM_ALL_PARTITIONS);
        getTopic().ifPresent(topic -> {
            for (int i = 0; i < topic.getPartitions(); i++) {
                txtPartition.addItem(String.valueOf(i));
            }
            txtPartition.setSelectedIndex(0);
        });
    }

    private Optional<Topic> getTopic() {
        final var nameTopic = comboTopic.getSelectedItem().toString();
        return Optional.ofNullable(topicSelected)
                .filter(topic -> topic.getId().getValue()
                        .equals(comboTopic.getSelectedItem().toString()))
                .map(Optional::of)
                .orElse(new TreeSet<>(listTopicUseCase.execute()).stream()
                        .filter(topic -> topic.getId().getValue().equals(nameTopic))
                        .findFirst());
    }

    private void initializeListeners() {
        table.setDefaultRenderer(Object.class, new MonochromeTableCellRenderer());
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

        comboTopic.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                populatePartitions();
            }
        });
        txtComboTopic = (JTextField) comboTopic.getEditor().getEditorComponent();
        txtComboTopic.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterTopics();
            }
        });
        txtComboTopic.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                populatePartitions();
            }
        });

        txtPartition.addItemListener(this::showOffset);

        btnPlus.addActionListener(e -> createConsumer());
        btnSubtract.addActionListener(e -> subtractConsumer());
        btnStart.addActionListener(e -> startConsumer());
        btnStop.addActionListener(e -> stopConsumer());

        tableListConsumers.setDefaultRenderer(Object.class, new MonochromeTableCellRenderer());
        tableListConsumers.setTableHeader(null);
        tableListConsumers.setDefaultEditor(Object.class, null);
        tableListConsumers.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                showConsumer();
            }
        });
        tableListConsumers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                showConsumer();
            }
        });
    }

    private void createTable() {
        model = new DefaultTableModel(COLUMN_NAMES, 0);
        table.setModel(model);

        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(250);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(65);
    }

    public List<MessageFilter> buildMessageFilter(Topic topic) {
        var messageFilters = new ArrayList<MessageFilter>();
        final var topicName = topic.getId().getValue();
        final var partitionSelected = txtPartition.getSelectedIndex() - 1;
        final var offset = Long.valueOf(txtOffset.getText());
        final var limit = 100L;

        if (partitionSelected < 0) {
            for (int partition = 0; partition < topic.getPartitions(); partition++) {
                final var offsetCommand = GetOffsetCommand.of(topic, partition);
                messageFilters.add(new MessageFilter(topicName, partition,
                        getLastOffsetTopicUseCase.execute(offsetCommand), limit));
            }

        } else {
            messageFilters.add(new MessageFilter(topicName, partitionSelected, offset, limit));
        }

        return messageFilters;
    }

    private void populateMessageTable(List<Message> listMessages) {
        listMessages.forEach(message -> model.addRow(
                new Object[]{message.getOffset(), message.getId().getValue(),
                        message.getTimestamp(),
                        message.getMessage(), HeaderParser.mapToString(message.getHeaders()),
                        message.getPartition()}));
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
        table.updateUI();
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
    }

    private void filterTopics() {
        final var text = txtComboTopic.getText();
        final var filters = text.split(" ");

        List<Topic> filteredTopics = new TreeSet<>(listTopicUseCase.execute()).stream()
                .filter(topic -> Arrays.stream(filters)
                        .allMatch(filter -> topic.getId().getValue().toLowerCase()
                                .contains(filter.toLowerCase())))
                .collect(Collectors.toList());

        comboTopic.removeAllItems();
        filteredTopics.stream()
                .map(Topic::getId)
                .map(TopicID::getValue)
                .forEach(comboTopic::addItem);
        txtComboTopic.setText(text);
        comboTopic.setPopupVisible(true);
    }

    private void createTableListConsumer() {
        modelListConsumer = new MyTableModel<>(List.of("topic"), ConsumerDto.class);
        tableListConsumers.setModel(modelListConsumer);
    }

    private void showConsumer() {
        isShowingConsumer = true;
        final var consumer = modelListConsumer.get(tableListConsumers.getSelectedRow());
        comboTopic.setSelectedItem(consumer.getTopic().getId().getValue());
        txtMessage.setText("");
        txtHeaders.setText("");
        lbRuntime.setText("Runtime: 0:00:00");
        createTable();
        populateMessageTable(consumer.getListMessages());
        if (consumer.isRunning()) {
            btnStart.setVisible(false);
            btnStop.setVisible(true);
        } else {
            btnStart.setVisible(true);
            btnStop.setVisible(false);
        }
        if (consumer.getMessageFilters().size() > 1) {
            txtPartition.setSelectedIndex(0);
        } else {
            consumer.getListMessages().stream().findFirst()
                    .ifPresent(message -> txtPartition.setSelectedItem(
                            message.getPartition().toString()));
        }
        isShowingConsumer = false;

    }

    private Boolean isConsumerDataUpdated() {
        Instant startTime = Instant.now();
        Instant endTime = startTime.plus(Duration.ofMillis(500));

        while (Instant.now().isBefore(endTime)) {
            if (FALSE.equals(isShowingConsumer)) {
                return true;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        return false;
    }

    private void createConsumer() {
        getTopic().ifPresent(topic -> {
            final var messageFilters = buildMessageFilter(topic);
            final var consumerDto = new ConsumerDto(topic, messageFilters);
            modelListConsumer.add(consumerDto);
        });
    }

    private void subtractConsumer() {
        var row = tableListConsumers.getSelectedRow();
        final var consumer = modelListConsumer.get(row);
        if (consumer.isRunning()) {
            JOptionPane.showMessageDialog(this, CANNOT_REMOVE_CONSUMER_BECAUSE_IS_RUNNING);
            return;
        }
        modelListConsumer.remove(row);
        if (tableListConsumers.getRowCount() > 0) {
            if (row > 0) {
                row -= 1;
            }
            tableListConsumers.changeSelection(row, 0, false, false);
            showConsumer();

        } else {
            createTableListConsumer();
            txtMessage.setText("");
            txtHeaders.setText("");
        }
        tableListConsumers.updateUI();
    }

    private void getRuntime() {
        schedulerRuntime = Executors.newScheduledThreadPool(1);
        schedulerRuntime.scheduleAtFixedRate(() -> {
            final var consumer = modelListConsumer.get(tableListConsumers.getSelectedRow());
            if (consumer.isRunning() && isConsumerDataUpdated()) {
                final var duration = Duration.between(consumer.getStartTime(), Instant.now());
                final var runtime = String.format("%d:%02d:%02d", duration.toHours(),
                        duration.toMinutesPart(), duration.toSecondsPart());
                lbRuntime.setText("Runtime: " + runtime);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private void startConsumer() {
        final int row = tableListConsumers.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, CANNOT_RUN_BECAUSE_NO_CONSUMER_HAS_BEEN_SELECTED);
            return;
        }
        final var consumer = modelListConsumer.get(row);
        consumer.start();
        startSchedulerMessage();
        btnStop.setVisible(true);
        btnStart.setVisible(false);
    }

    private void stopConsumer() {
        final var consumer = modelListConsumer.get(tableListConsumers.getSelectedRow());
        consumer.stop();
        final var numberRunningConsumer = modelListConsumer.getData().stream()
                .filter(ConsumerDto::isRunning)
                .count();
        if (numberRunningConsumer == 0) {
            schedulerRuntime.shutdown();
            threadMessage = null;
        }
        btnStart.setVisible(true);
        btnStop.setVisible(false);
    }

    private void setupIcons() {
        var iconBtnPlus = new FlatSVGIcon("icons/add_outline_icon.svg", 18, 18,
                getClass().getClassLoader());
        iconBtnPlus.setColorFilter(SetupColor.getIconColor());
        btnPlus.setIcon(iconBtnPlus);

        var iconBtnSubtract = new FlatSVGIcon("icons/minus_outline_icon.svg", 18, 18,
                getClass().getClassLoader());
        iconBtnSubtract.setColorFilter(SetupColor.getIconColor());
        btnSubtract.setIcon(iconBtnSubtract);

        var iconBtnStar = new FlatSVGIcon("icons/outline_play_icon.svg", 18, 18,
                getClass().getClassLoader());
        iconBtnStar.setColorFilter(SetupColor.getIconColor());
        btnStart.setIcon(iconBtnStar);

        var iconBtnStop = new FlatSVGIcon("icons/stop_circle_line_icon.svg", 23, 23,
                getClass().getClassLoader());
        iconBtnStop.setColorFilter(SetupColor.getIconColor());
        btnStop.setIcon(iconBtnStop);
    }

    private void startSchedulerMessage() {
        getRuntime();
        if (threadMessage == null) {
            threadMessage = new Thread(() -> {
                while (schedulerMessage) {
                    final var listConsumer = modelListConsumer.getData().stream()
                            .filter(ConsumerDto::isRunning)
                            .map(ConsumerDto::withCleanListNewMessages)
                            .collect(Collectors.toList());
                    if (listConsumer.isEmpty()) {
                        schedulerMessage = false;
                    }
                    final var messageFilters = listConsumer.stream()
                            .map(ConsumerDto::getMessageFilters)
                            .flatMap(Collection::stream)
                            .collect(Collectors.toList());

                    final List<Message> result = listMessageUseCase.execute(messageFilters);

                    result.stream()
                            .forEach(message -> listConsumer.stream()
                                    .filter(m -> m.getTopic().getId().getValue()
                                            .equals(message.getTopicName()))
                                    .findFirst()
                                    .ifPresent(consumer -> {
                                        consumer.getListMessages().add(message);
                                        consumer.getListNewMessages().add(message);
                                    }));

                    if (isConsumerDataUpdated() && FALSE.equals(result.isEmpty())) {
                        final var consumerSelected = modelListConsumer.get(
                                tableListConsumers.getSelectedRow());
                        listConsumer.stream().filter(consumerDto -> consumerDto.getId()
                                        .equals(consumerSelected.getId()))
                                .findFirst()
                                .ifPresent(consumerDto -> {
                                    populateMessageTable(consumerDto.getListNewMessages());
                                    consumerDto.withCleanListNewMessages();
                                });
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            });
            threadMessage.start();
        }
    }

}

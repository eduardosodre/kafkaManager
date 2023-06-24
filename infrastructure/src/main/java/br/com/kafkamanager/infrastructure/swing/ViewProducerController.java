package br.com.kafkamanager.infrastructure.swing;

import static java.lang.Boolean.TRUE;

import br.com.kafkamanager.application.message.create.CreateMessageUseCase;
import br.com.kafkamanager.application.topic.list.ListTopicUseCase;
import br.com.kafkamanager.domain.exceptions.NotificationException;
import br.com.kafkamanager.domain.topic.Topic;
import br.com.kafkamanager.domain.topic.TopicID;
import br.com.kafkamanager.infrastructure.Main;
import br.com.kafkamanager.infrastructure.message.CreateMessageCommandDto;
import br.com.kafkamanager.infrastructure.swing.util.HeaderParser;
import br.com.kafkamanager.infrastructure.swing.util.JFileChooserUtil;
import br.com.kafkamanager.infrastructure.swing.util.JOptionUtil;
import br.com.kafkamanager.infrastructure.swing.util.MonochromeTableCellRenderer;
import br.com.kafkamanager.infrastructure.swing.util.MyTableModel;
import br.com.kafkamanager.infrastructure.swing.util.SetupColor;
import br.com.kafkamanager.infrastructure.util.ContextUtil;
import br.com.kafkamanager.infrastructure.util.JsonUtil;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ViewProducerController extends ViewProducer {

    private static final String[] COLUMN_NAMES = {"Key", "Value"};
    private static final String COULD_NOT_LOAD_FILE_MESSAGE = "Could not load the file";
    private static final String FILE_SAVED_SUCCESSFULLY_MESSAGE = "A File saved successfully!";
    private static final String COULD_NOT_SAVE_FILE_MESSAGE = "Could not save the file!";
    private static final String MESSAGE_CREATED_ERROR = "Could not create the message";

    private final ListTopicUseCase listTopicUseCase;
    private final CreateMessageUseCase createMessageUseCase;
    private final String topicName;
    private DefaultTableModel modelHeader;
    private MyTableModel<CreateMessageCommandDto> modelListProducer;
    private DefaultTableModel modelSentProducers;
    private JTextField txtComboTopic;
    private int rowTableListProducerShowed;
    private Boolean producerDataShowedUpdated = true;

    public ViewProducerController(String topicName) {
        super();
        listTopicUseCase = ContextUtil.getBean(ListTopicUseCase.class);
        createMessageUseCase = ContextUtil.getBean(CreateMessageUseCase.class);
        this.topicName = topicName;
        start();
        setVisible(true);
    }

    private void start() {
        initializeTopics();
        initializeListeners();
        setupIcons();
        createTableHeader();
        createTableListProducer();
        createTableSentProducers();
        plusHeader();
        loadCreateMessageCommandFromFiles();
    }

    private void initializeTopics() {
        final var topics = new TreeSet<>(listTopicUseCase.execute());
        for (final var kafkaTopicDto : topics) {
            comboTopic.addItem(kafkaTopicDto.getId().getValue());
        }
        comboTopic.setSelectedItem(topicName);
    }

    private void initializeListeners() {
        btnProduce.addActionListener(e -> producer());
        btnLoadProducer.addActionListener(e -> load());
        btnSave.addActionListener(e -> save());
        btnPlus.addActionListener(e -> plusHeader());
        btnSubtract.addActionListener(e -> subtract());
        btnImportHeaderKafdrop.addActionListener(e -> importHeadersFromKafdrop());
        btnFormatter.addActionListener(e -> txtValue.setText(JsonUtil.format(txtValue.getText())));
        btnNewProducer.addActionListener(e -> newProducer());
        btnDeleteProducer.addActionListener(e -> deleteProducer());

        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterTableListProduces();
            }
        });

        comboTopic.setEditable(true);
        txtComboTopic = (JTextField) comboTopic.getEditor().getEditorComponent();
        txtComboTopic.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterTopics();
            }
        });

        updateFromComponent(txtComboTopic);
        updateFromComponent(comboTopic);
        updateFromComponent(txtKey);
        updateFromComponent(txtValue);
        updateFromComponent(tableHeader);

        tableHeader.setDefaultRenderer(Object.class, new MonochromeTableCellRenderer());

        tableListProducer.setDefaultRenderer(Object.class, new MonochromeTableCellRenderer());
        tableListProducer.setTableHeader(null);
        tableListProducer.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                showProducer();
            }
        });
        tableListProducer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                showProducer();
            }
        });

        tableSentProducers.setDefaultRenderer(Object.class, new MonochromeTableCellRenderer());
        tableSentProducers.setTableHeader(null);
        tableSentProducers.setRowHeight(60);
        tableSentProducers.setDefaultEditor(Object.class, null);
    }

    private void updateFromComponent(JComponent txtKey) {
        txtKey.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                producerDataShowedUpdated = false;
            }

            @Override
            public void focusLost(FocusEvent e) {
                updateProducer();
            }
        });
    }

    private void createTableHeader() {
        modelHeader = new DefaultTableModel(COLUMN_NAMES, 0);
        tableHeader.setModel(modelHeader);
    }

    private void createTableListProducer() {
        modelListProducer = new MyTableModel<>(List.of("description"),
                CreateMessageCommandDto.class);
        tableListProducer.setModel(modelListProducer);
    }

    private void createTableSentProducers() {
        modelSentProducers = new DefaultTableModel(new String[]{""}, 0);
        tableSentProducers.setModel(modelSentProducers);
    }

    private void producer() {
        try {
            if (TRUE.equals(isProducerDataUpdated())) {
                final var commandDto = modelListProducer.get(tableListProducer.getSelectedRow());
                final var message = createMessageUseCase.execute(
                        commandDto.buildCreateMessageCommand());

                final var stringBuilder = new StringBuilder("<html>");
                stringBuilder.append(
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                stringBuilder.append("<br>");
                stringBuilder.append(">" + message.getTopicName());
                stringBuilder.append("<br>");
                stringBuilder.append("Offset: " + message.getOffset());
                stringBuilder.append("</html>");

                commandDto.getSentProducers().add(stringBuilder.toString());
                modelSentProducers.addRow(new Object[]{stringBuilder.toString()});
                var verticalScrollBar = scrollPaneListProduced.getVerticalScrollBar();
                verticalScrollBar.setValue(verticalScrollBar.getMaximum());
                tableSentProducers.updateUI();
                verticalScrollBar = scrollPaneListProduced.getVerticalScrollBar();
                verticalScrollBar.setValue(verticalScrollBar.getMaximum());

            }
        } catch (NotificationException e) {
            JOptionPane.showMessageDialog(null,
                    MESSAGE_CREATED_ERROR + ": " + e.getFormattedErrors());
        }
    }

    private void load() {
        final var file = JFileChooserUtil.chooseFile();
        if (file == null) {
            return;
        }
        try {
            final var kafkaProducerDto = JsonUtil.readJsonFile(file, CreateMessageCommandDto.class);
            if (Objects.isNull(kafkaProducerDto.getDescription().isBlank())) {
                kafkaProducerDto.setDescription("*Description");
            }
            modelListProducer.add(kafkaProducerDto);
        } catch (FileNotFoundException e) {
            showMessageDialog(COULD_NOT_LOAD_FILE_MESSAGE);
        }
    }

    private Boolean isProducerDataUpdated() {
        Instant startTime = Instant.now();
        Instant endTime = startTime.plus(Duration.ofMillis(500));

        while (Instant.now().isBefore(endTime)) {
            if (TRUE.equals(producerDataShowedUpdated)) {
                return true;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        return false;
    }

    private void showProducer() {
        if (TRUE.equals(isProducerDataUpdated())) {
            rowTableListProducerShowed = tableListProducer.getSelectedRow();
            final var kafkaProducerDto = modelListProducer.get(rowTableListProducerShowed);
            tabbedPane.setVisible(true);
            comboTopic.setSelectedItem(kafkaProducerDto.getTopicName());
            txtKey.setText(kafkaProducerDto.getKey());
            txtValue.setText(kafkaProducerDto.getMessage());

            populateTableWithHeaders(kafkaProducerDto.getHeaders());
            populateTableSentProducers(kafkaProducerDto.getSentProducers());
        }
    }

    private Set<String> listFiles() throws IOException {
        File directory = new File(Main.folder);
        if (!directory.exists()) {
            directory.mkdir();
        }

        try (Stream<Path> stream = Files.list(Paths.get(directory.getPath()))) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::toString)
                    .collect(Collectors.toSet());
        }
    }

    private void loadCreateMessageCommandFromFiles() {
        List<CreateMessageCommandDto> listCreateMessageCommands = null;
        try {
            listCreateMessageCommands = listFiles().stream()
                    .map(file -> {
                        try {
                            final var kafkaProducerDto = JsonUtil.readJsonFile(file,
                                    CreateMessageCommandDto.class);
                            kafkaProducerDto.setFile(file);
                            return kafkaProducerDto;
                        } catch (FileNotFoundException e) {
                            System.err.println("Cannot read file " + file);
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (Boolean.FALSE.equals(listCreateMessageCommands.isEmpty())) {
                listCreateMessageCommands.sort((p1, p2) -> {
                    if (p1.getDescription().equals(p2.getDescription())) {
                        return p1.getTopicName().compareTo(p2.getTopicName());
                    } else {
                        return p1.getDescription().compareTo(p2.getDescription());
                    }
                });
                modelListProducer.setData(listCreateMessageCommands);
                tableListProducer.changeSelection(0, 0, false, false);
                showProducer();
            }
        } catch (IOException e) {
            System.err.println("Error load producers " + e);
        }
    }

    private void save() {
        final var kafkaProducerDto = buildMessageCommandDto(
                modelListProducer.get(tableListProducer.getSelectedRow()));
        final String file;
        if (Objects.nonNull(kafkaProducerDto.getFile())) {
            file = kafkaProducerDto.getFile();
        } else {
            file = JFileChooserUtil.chooseFile();
        }

        try {
            modelListProducer.setValueAt(kafkaProducerDto.getDescription().replace("*", ""),
                    tableListProducer.getSelectedRow(), 0);
            JsonUtil.writeJsonFile(kafkaProducerDto, file);
            kafkaProducerDto.setFile(file);
            showMessageDialog(FILE_SAVED_SUCCESSFULLY_MESSAGE);
        } catch (IOException e) {
            showMessageDialog(COULD_NOT_SAVE_FILE_MESSAGE);
        }
    }

    private void plusHeader() {
        modelHeader.addRow(new Object[]{"", ""});
    }

    private void subtract() {
        modelHeader.removeRow(tableHeader.getSelectedRow());
    }

    private CreateMessageCommandDto buildMessageCommandDto() {
        return buildMessageCommandDto(null);
    }

    private CreateMessageCommandDto buildMessageCommandDto(CreateMessageCommandDto producer) {
        final var toString = Optional.ofNullable(producer).map(Object::toString).orElse("");
        final var map = new HashMap<String, String>();
        final var totalRow = tableHeader.getRowCount();
        final var keyProducer = txtKey.getText().strip();
        for (int i = 0; i < totalRow; i++) {
            String key = tableHeader.getValueAt(i, 0).toString().strip();
            String value = tableHeader.getValueAt(i, 1).toString().strip();
            if (!value.isBlank()) {
                map.put(key, value);
            }
        }
        final var listSent = new ArrayList<String>();
        for (int i = 0; i < tableSentProducers.getRowCount(); i++) {
            String value = tableSentProducers.getValueAt(i, 0).toString().strip();
            listSent.add(value);
        }

        return Optional.ofNullable(producer)
                .map(p -> {
                    p.update(p.getDescription(), keyProducer,
                            String.valueOf(comboTopic.getSelectedItem()),
                            txtValue.getText().strip(), map, listSent);

                    if (Boolean.FALSE.equals(toString.isEmpty()) && !toString.equals(p.toString())) {
                        modelListProducer.setValueAt("*" + p.getDescription().replace("*", ""),
                                rowTableListProducerShowed, 0);
                        p.setDescription("*" + p.getDescription().replace("*", ""));
                    }
                    return p;
                })
                .orElse(CreateMessageCommandDto.of("*Description", keyProducer,
                        String.valueOf(comboTopic.getSelectedItem()),
                        txtValue.getText().strip(), map, new ArrayList<>(), null));
    }

    private void importHeadersFromKafdrop() {
        final var header = JOptionUtil.showCustomInputDialog("Copy headers from Kafdrop:",
                "Complete");
        if (header == null || header.isBlank()) {
            return;
        }
        populateTableWithHeaders(HeaderParser.parseEventString(header));
    }

    private void populateTableWithHeaders(Map<String, String> headerMap) {
        createTableHeader();
        for (final var entry : headerMap.entrySet()) {
            modelHeader.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
    }

    private void populateTableSentProducers(List<String> listSent) {
        createTableSentProducers();
        if (listSent != null) {
            listSent.forEach((value) -> modelSentProducers.addRow(new Object[]{value}));
        }
    }

    private void showMessageDialog(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private void setupIcons() {
        var iconBtnLoadProducer = new FlatSVGIcon("icons/upload_icon.svg", 20, 20,
                getClass().getClassLoader());
        iconBtnLoadProducer.setColorFilter(SetupColor.getIconColor());
        btnLoadProducer.setIcon(iconBtnLoadProducer);

        var iconBtnNewProducer = new FlatSVGIcon(
                getClass().getClassLoader().getResource("icons/add_outline_icon.svg"));
        iconBtnNewProducer.setColorFilter(SetupColor.getIconColor());
        btnNewProducer.setIcon(iconBtnNewProducer);

        var iconBtnDeleteProducer = new FlatSVGIcon("icons/trash_icon.svg", 20, 20,
                getClass().getClassLoader());
        iconBtnDeleteProducer.setColorFilter(SetupColor.getIconColor());
        btnDeleteProducer.setIcon(iconBtnDeleteProducer);

        var iconBtnSave = new FlatSVGIcon("icons/disk_save_floppy_icon.svg", 20, 20,
                getClass().getClassLoader());
        iconBtnSave.setColorFilter(SetupColor.getIconColor());
        btnSave.setIcon(iconBtnSave);

        var iconBtnProduce = new FlatSVGIcon("icons/mail_send_line_icon.svg", 20, 20,
                getClass().getClassLoader());
        iconBtnProduce.setColorFilter(SetupColor.getIconColor());
        btnProduce.setIcon(iconBtnProduce);

        var iconBtnPlus = new FlatSVGIcon("icons/add_outline_icon.svg", 18, 18,
                getClass().getClassLoader());
        iconBtnPlus.setColorFilter(SetupColor.getIconColor());
        btnPlus.setIcon(iconBtnPlus);

        var iconBtnSubtract = new FlatSVGIcon("icons/minus_outline_icon.svg", 18, 18,
                getClass().getClassLoader());
        iconBtnSubtract.setColorFilter(SetupColor.getIconColor());
        btnSubtract.setIcon(iconBtnSubtract);

        var iconBtnImportHeaderKafdrop = new FlatSVGIcon("icons/upload_icon.svg", 18, 18,
                getClass().getClassLoader());
        iconBtnImportHeaderKafdrop.setColorFilter(SetupColor.getIconColor());
        btnImportHeaderKafdrop.setIcon(iconBtnImportHeaderKafdrop);

        var iconBtnFormatter = new FlatSVGIcon("icons/code_brackets_icon.svg", 16, 16,
                getClass().getClassLoader());
        iconBtnFormatter.setColorFilter(SetupColor.getIconColor());
        btnFormatter.setIcon(iconBtnFormatter);

        var iconTxtSearch = new FlatSVGIcon("icons/search_icon.svg", 16, 16,
                getClass().getClassLoader());
        iconTxtSearch.setColorFilter(SetupColor.getIconColor());
        txtSearch.setShowClearButton(true);
        txtSearch.setLeadingIcon(iconTxtSearch);
    }

    private void newProducer() {
        modelListProducer.add(buildMessageCommandDto());
    }

    private void deleteProducer() {
        var row = tableListProducer.getSelectedRow();
        modelListProducer.remove(row);

        if (tableListProducer.getRowCount() > 0) {
            if (row > 0) {
                row -= 1;
            }
            tableListProducer.changeSelection(row, 0, false, false);
            showProducer();

        } else {
            tabbedPane.setVisible(false);
        }
        tableListProducer.updateUI();
    }

    private void updateProducer() {
        buildMessageCommandDto(modelListProducer.get(rowTableListProducerShowed));
        producerDataShowedUpdated = true;
    }

    private void filterTopics() {
        final var text = txtComboTopic.getText();
        final var filters = text.split(" ");

        List<String> filteredTopics = listTopicUseCase.execute().stream()
                .map(Topic::getId)
                .map(TopicID::getValue)
                .filter(item -> Arrays.stream(filters)
                        .allMatch(filter -> item.toLowerCase().contains(filter.toLowerCase())))
                .sorted(Comparable::compareTo)
                .collect(Collectors.toList());

        comboTopic.removeAllItems();
        filteredTopics.forEach(comboTopic::addItem);
        txtComboTopic.setText(text);
        comboTopic.setPopupVisible(true);
    }

    private void filterTableListProduces() {
        if (TRUE.equals(isProducerDataUpdated())) {
            final var text = txtSearch.getText();
            final var filters = text.split(" ");

            for (int i = 0; i < modelListProducer.getData().size(); i++) {
                var description = modelListProducer.getData().get(i).getDescription();
                if (Arrays.stream(filters)
                        .allMatch(filter -> description.toLowerCase().contains(filter.toLowerCase()))) {
                    tableListProducer.changeSelection(i, 0, false, false);
                    break;
                }
            }
        }
    }
}
package br.com.kafkamanager.infrastructure.swing;

import br.com.kafkamanager.application.topic.delete.DeleteTopicUseCase;
import br.com.kafkamanager.application.topic.list.ListTopicUseCase;
import br.com.kafkamanager.domain.topic.Topic;
import br.com.kafkamanager.domain.topic.TopicID;
import br.com.kafkamanager.infrastructure.MyConfig;
import br.com.kafkamanager.infrastructure.preference.UserPreference;
import br.com.kafkamanager.infrastructure.preference.UserPreferenceService;
import br.com.kafkamanager.infrastructure.swing.themes.ThemeType;
import br.com.kafkamanager.infrastructure.swing.util.JOptionUtil;
import br.com.kafkamanager.infrastructure.swing.util.SetupColor;
import br.com.kafkamanager.infrastructure.util.ContextUtil;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatUIUtils;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;

public class ViewDashboardController extends ViewDashboard {

    private static final String[] COLUMN_NAMES = {"Name", "Partitions", "Count"};
    private static final String NO_TOPIC_SELECTED_MESSAGE = "A topic must be selected in the table.";
    private final ListTopicUseCase listTopicUseCase;
    private final DeleteTopicUseCase deleteTopicUseCase;
    private List<Topic> listTopics;
    private DefaultTableModel model;
    private MyConfig myConfig;


    public ViewDashboardController() {
        super();
        listTopicUseCase = ContextUtil.getBean(ListTopicUseCase.class);
        deleteTopicUseCase = ContextUtil.getBean(DeleteTopicUseCase.class);
        myConfig = ContextUtil.getBean(MyConfig.class);
        start();
        setVisible(true);
    }

    private void start() {
    	jContentPane.setBackground(setupColorJContentPane());
        setupIcons();
        setupButtons();
        setupIcons();
        setupSearch();
        showTopics();
        showInfoConnection();
    }

    private Color setupColorJContentPane() {
    	if (FlatLaf.isLafDark()) {
    		Color colorTheme = UIManager.getColor("Panel.background");
    		return FlatUIUtils.nonUIResource(SetupColor.lightenColor(colorTheme, 5));
    	}
    	return FlatUIUtils.nonUIResource(Color.WHITE);
    }

    private void setupButtons() {
        btnConsumer.addActionListener(e -> consumer());
        btnCreateTopic.addActionListener(e -> createTopic());
        btnProducer.addActionListener(e -> producer());
        btnDeleteTopic.addActionListener(e -> deleteTopic());
        btnUpdate.addActionListener(e -> showTopics());
        btnClose.addActionListener(e -> closingProgram());

        Arrays.stream(ThemeType.values()).forEach(themeType -> comboTheme.addItem(themeType.description));
        comboTheme.addItemListener(this::updateTheme);

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closingProgram();
            }
        });
    }

    private void closingProgram() {
        var server = Optional.ofNullable(myConfig).map(MyConfig::getServer).orElse("localhost:9092");
        UserPreferenceService.savePreferences(new UserPreference(server, comboTheme.getSelectedItem().toString()));

        final var kafkaProducer = ContextUtil.getBean(KafkaProducer.class);
        final var kafkaAdminClient = ContextUtil.getBean(AdminClient.class);
        final var kafkaConsumer = ContextUtil.getBean(KafkaConsumer.class);

        kafkaProducer.close();
        kafkaAdminClient.close();
        kafkaConsumer.close();

        System.exit(0);
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
        final var filters = txtSearch.getText().toLowerCase().split(" ");
        final var filteredTopics = listTopics.stream()
    			.filter(topic -> Arrays.stream(filters).allMatch(filter -> topic.getId().getValue().toLowerCase().contains(filter.toLowerCase())))
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

        table.getColumnModel().getColumn(0).setPreferredWidth(400);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
    }

    private void populateTopicTable(List<Topic> listKafka) {
        createTable();

        listKafka.sort((p1, p2) -> p1.getId().getValue().compareTo(p2.getId().getValue()));
        listKafka.forEach(kafkaTopicDto -> model.addRow(
            new Object[]{kafkaTopicDto.getId().getValue(), kafkaTopicDto.getPartitions().toString(),
                kafkaTopicDto.getReplications().toString()}));
    }

    private void consumer() {
        final Integer selected = getSelectedRow();
        if (selected == null) {
            return;
        }
        final var topicName = table.getValueAt(selected, 0).toString();
        listTopics.stream()
            .filter(topic -> topic.getId().getValue().equalsIgnoreCase(topicName)).findFirst()
            .ifPresent(ViewConsumerController::new);
    }

    private void producer() {
        final Integer selected = table.getSelectedRow();
        var topicName = "";
        if (selected >= 0) {
        	topicName = table.getValueAt(selected, 0).toString();
        }
        new ViewProducerController(topicName);
    }

    private Integer getSelectedRow() {
        final var selected = table.getSelectedRow();
        if (selected < 0) {
            JOptionPane.showMessageDialog(this, NO_TOPIC_SELECTED_MESSAGE);
            return null;
        }
        return selected;
    }

    private void createTopic() {
        new ViewCreateTopicController(this);
        showTopics();
    }

    private void deleteTopic() {
        final Integer selected = getSelectedRow();
        if (selected == null) {
            return;
        }
        boolean confirm = JOptionUtil.showCustomConfirmDialog(
            "Do you really want to delete the topic?", "Confirm");
        if (confirm) {
            final var topicName = table.getValueAt(selected, 0).toString();
            deleteTopicUseCase.execute(TopicID.from(topicName));
            showTopics();
        }
    }

    private void updateTheme(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            String selectedTheme = comboTheme.getSelectedItem().toString();

            ThemeType.startTheme(selectedTheme);

            jContentPane.setBackground(setupColorJContentPane());
            FlatLaf.updateUI();
        }
    }

    private void setupIcons() {
    	var iconLbIcon = new FlatSVGIcon("icons/apache_kafka-icon.svg", 30, 30, getClass().getClassLoader());
		iconLbIcon.setColorFilter(SetupColor.getIconColor());
        lbLogoIcon.setIcon(iconLbIcon);

    	var iconLbMenuItemTopicIcon = new FlatSVGIcon(getClass().getClassLoader().getResource("icons/servers_icon.svg"));
        iconLbMenuItemTopicIcon.setColorFilter(SetupColor.getIconColor());
        lbMenuItemTopicIcon.setIcon(iconLbMenuItemTopicIcon);

        var iconBtnProducer = new FlatSVGIcon("icons/mail_send_line_icon.svg", 20, 20, getClass().getClassLoader());
        iconBtnProducer.setColorFilter(SetupColor.getIconColor());
        btnProducer.setIcon(iconBtnProducer);

        var iconBtnConsumer = new FlatSVGIcon("icons/mail_download_line_email_icon.svg", 20, 20, getClass().getClassLoader());
        iconBtnConsumer.setColorFilter(SetupColor.getIconColor());
        btnConsumer.setIcon(iconBtnConsumer);

        var iconBtnUpdate = new FlatSVGIcon("icons/reload_icon.svg", 20, 20, getClass().getClassLoader());
        iconBtnUpdate.setColorFilter(SetupColor.getIconColor());
        btnUpdate.setIcon(iconBtnUpdate);

        var iconBtnClose = new FlatSVGIcon("icons/logout_box_r_line_icon.svg", 20, 20, getClass().getClassLoader());
        iconBtnClose.setColorFilter(SetupColor.getIconColor());
        btnClose.setIcon(iconBtnClose);

        var iconBtnCreateTopic = new FlatSVGIcon("icons/add_outline_icon.svg", 18, 18, getClass().getClassLoader());
        iconBtnCreateTopic.setColorFilter(SetupColor.getIconColor());
        btnCreateTopic.setIcon(iconBtnCreateTopic);

        var iconBtnDeleteTopic = new FlatSVGIcon("icons/trash_icon.svg", 18, 18, getClass().getClassLoader());
        iconBtnDeleteTopic.setColorFilter(SetupColor.getIconColor());
        btnDeleteTopic.setIcon(iconBtnDeleteTopic);

        var iconTxtSearch = new FlatSVGIcon("icons/search_icon.svg", 16, 16, getClass().getClassLoader());
        iconTxtSearch.setColorFilter(SetupColor.getIconColor());
        txtSearch.setShowClearButton(true);
        txtSearch.setLeadingIcon(iconTxtSearch);
    }

    private void showInfoConnection( ) {
    	String server = Optional.ofNullable(myConfig).map(MyConfig::getServer).orElse("");
    	lbInfoConnection.setText(server);
    }
}

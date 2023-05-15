package br.com.kafkamanager.infrastructure.swing;

import static javax.swing.SwingConstants.TOP;

import br.com.kafkamanager.domain.topic.Topic;
import br.com.kafkamanager.infrastructure.swing.util.TransparentJPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

public class ViewConsumer extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel jContentPane = null;
    protected JScrollPane scrollPane;
    protected JTable table;
    protected JButton btnStop;
    protected JPanel panel;
    protected JLabel lblPartition;
    protected JComboBox<String> txtPartition;
    protected JLabel lblOffset;
    protected JTextField txtOffset;
    protected JScrollPane scrollHeader;
    protected JScrollPane scrollMessage;
    protected JTextPane txtHeaders;
    protected RSyntaxTextArea txtMessage;
    protected JLabel lbOffset;
    protected JTabbedPane tabbedPane;
    protected JPanel panelSetup;
    protected JPanel panelSetupHeader;
    protected JComboBox<Topic> comboTopic;
    protected JScrollPane scrollPaneListConsumers;
    protected JButton btnStart;
    protected JTable tableListConsumers;
    protected JButton btnSubtract;
    protected JButton btnPlus;
    protected JLabel lbRuntime;
    protected JPanel panelOffset;
    private JSplitPane splitPane;

    public ViewConsumer() {
        super();
        initialize();
    }

    private void initialize() {
        this.setSize(1000, 600);
        this.setTitle("Kafka Consumer");
        this.setContentPane(getJContentPane());
        this.setResizable(false);
        this.setLocationRelativeTo(getOwner());
        getJContentPane().add(getPanelSetup());
        getJContentPane().add(getSplitPane());
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getPanel());
        }
        return jContentPane;
    }

    private JTabbedPane getTabbedPane() {
        if (tabbedPane == null) {
            tabbedPane = new JTabbedPane(TOP);
            tabbedPane.setFont(UIManager.getFont("h4.font"));
            tabbedPane.addTab("Message", null, getScrollMessage(), null);
            tabbedPane.addTab("Headers", null, getScrollHeader(), null);
        }
        return tabbedPane;
    }

    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane();
            scrollPane.setViewportView(getTable());
        }
        return scrollPane;
    }

    private JTable getTable() {
        if (table == null) {
            table = new JTable();
        }
        return table;
    }

    private JButton getBtnStop() {
        if (btnStop == null) {
            btnStop = new JButton("Stop");
            btnStop.setBounds(571, 0, 151, 25);
            btnStop.setVisible(false);
        }
        return btnStop;
    }

    private JPanel getPanel() {
        if (panel == null) {
            panel = new TransparentJPanel();
            panel.setBounds(252, 525, 722, 25);
            panel.setLayout(null);
            panel.add(getBtnStop());
            panel.add(getBtnStart());
            panel.add(getLbRuntime());
        }
        return panel;
    }

    private JLabel getLblPartition() {
        if (lblPartition == null) {
            lblPartition = new JLabel("Partition:");
            lblPartition.setBounds(10, 40, 71, 25);
        }
        return lblPartition;
    }

    private JComboBox<String> getTxtPartition() {
        if (txtPartition == null) {
            txtPartition = new JComboBox<>();
            txtPartition.setBounds(10, 69, 79, 25);
        }
        return txtPartition;
    }

    private JLabel getLblOffset() {
        if (lblOffset == null) {
            lblOffset = new JLabel("Offset:");
            lblOffset.setBounds(89, 0, 59, 25);
        }
        return lblOffset;
    }

    private JTextField getTxtOffset() {
        if (txtOffset == null) {
            txtOffset = new JTextField();
            txtOffset.setBounds(89, 28, 141, 25);
            txtOffset.setColumns(10);
            txtOffset.setText("0");
        }
        return txtOffset;
    }

    private JLabel getLbOffset() {
        if (lbOffset == null) {
            lbOffset = new JLabel();
            lbOffset.setBounds(0, 59, 230, 25);
            lbOffset.setText("0");
        }
        return lbOffset;
    }

    private JScrollPane getScrollHeader() {
        if (scrollHeader == null) {
            scrollHeader = new JScrollPane();
            scrollHeader.setViewportView(getTxtHeaders());
        }
        return scrollHeader;
    }

    private JScrollPane getScrollMessage() {
        if (scrollMessage == null) {
            scrollMessage = new JScrollPane();
            scrollMessage.setViewportView(getTxtMessage());
        }
        return scrollMessage;
    }

    private JTextPane getTxtHeaders() {
        if (txtHeaders == null) {
            txtHeaders = new JTextPane();
        }
        return txtHeaders;
    }

    private RSyntaxTextArea getTxtMessage() {
        if (txtMessage == null) {
            txtMessage = new RSyntaxTextArea();
            txtMessage.setBackground(UIManager.getColor("TextArea.background"));
            txtMessage.setFont(UIManager.getFont("TextArea.font"));
            txtMessage.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON_WITH_COMMENTS);
            txtMessage.setCodeFoldingEnabled(true);
        }
        return txtMessage;
    }

    private JPanel getPanelSetup() {
        if (panelSetup == null) {
            panelSetup = new JPanel();
            panelSetup.setBounds(0, 0, 250, 561);
            panelSetup.setLayout(null);
            panelSetup.add(getPanelSetupHeader());
            panelSetup.add(getScrollPaneListConsumers());
            panelSetup.add(getBtnSubtract());
            panelSetup.add(getBtnPlus());
        }
        return panelSetup;
    }

    private JPanel getPanelSetupHeader() {
        if (panelSetupHeader == null) {
            panelSetupHeader = new JPanel();
            panelSetupHeader.setBounds(0, 0, 250, 129);
            panelSetupHeader.setLayout(null);
            panelSetupHeader.add(getComboTopic());
            panelSetupHeader.add(getLblPartition());
            panelSetupHeader.add(getTxtPartition());
            panelSetupHeader.add(getPanelOffset());
        }
        return panelSetupHeader;
    }

    private JComboBox<Topic> getComboTopic() {
        if (comboTopic == null) {
            comboTopic = new JComboBox<>();
            comboTopic.setBounds(10, 11, 230, 25);
        }
        return comboTopic;
    }

    private JScrollPane getScrollPaneListConsumers() {
        if (scrollPaneListConsumers == null) {
            scrollPaneListConsumers = new JScrollPane();
            scrollPaneListConsumers.setBounds(10, 130, 230, 388);
            scrollPaneListConsumers.setViewportView(getTableListConsumers());
        }
        return scrollPaneListConsumers;
    }

    private JButton getBtnStart() {
        if (btnStart == null) {
            btnStart = new JButton("Start");
            btnStart.setBounds(571, 0, 151, 25);
        }
        return btnStart;
    }

    private JTable getTableListConsumers() {
        if (tableListConsumers == null) {
            tableListConsumers = new JTable();
        }
        return tableListConsumers;
    }

    private JButton getBtnSubtract() {
        if (btnSubtract == null) {
            btnSubtract = new JButton("");
            btnSubtract.setBounds(205, 525, 35, 25);
        }
        return btnSubtract;
    }

    private JButton getBtnPlus() {
        if (btnPlus == null) {
            btnPlus = new JButton("");
            btnPlus.setBounds(165, 525, 35, 25);
        }
        return btnPlus;
    }

    private JLabel getLbRuntime() {
        if (lbRuntime == null) {
            lbRuntime = new JLabel("Runtime: 0:00:00");
            lbRuntime.setHorizontalAlignment(SwingConstants.RIGHT);
            lbRuntime.setBounds(300, 0, 261, 25);
        }
        return lbRuntime;
    }

    private JPanel getPanelOffset() {
        if (panelOffset == null) {
            panelOffset = new JPanel();
            panelOffset.setOpaque(false);
            panelOffset.setVisible(false);
            panelOffset.setBounds(10, 40, 230, 85);
            panelOffset.setLayout(null);
            panelOffset.add(getLblOffset());
            panelOffset.add(getTxtOffset());
            panelOffset.add(getLbOffset());
        }
        return panelOffset;
    }

    private JSplitPane getSplitPane() {
        if (splitPane == null) {
            splitPane = new JSplitPane();
            splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            splitPane.setBounds(252, 0, 722, 518);
            splitPane.setLeftComponent(getScrollPane());
            splitPane.setRightComponent(getTabbedPane());
            splitPane.setDividerLocation(130);
        }
        return splitPane;
    }
}

package br.com.kafkamanager.infrastructure.swing;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class ViewDashboard extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel jContentPane = null;
    protected JScrollPane scrollPane;
    protected JTable table;
    protected JButton btnProducer;
    protected JButton btnConsumer;
    protected JButton btnClose;
    protected JButton btnCreateTopic;
    protected JPanel panel;
    protected JTextField txtSearch;
    protected JButton btnDeleteTopic;

    public ViewDashboard() {
        super();
        initialize();
        setVisible(true);
    }

    private void initialize() {
        this.setSize(915, 395);
        this.setTitle("Kafka Dashboard");
        this.setContentPane(getJContentPane());
        this.setResizable(false);
        this.setLocationRelativeTo(getOwner());
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getScrollPane());
            jContentPane.add(getPanel());
            jContentPane.add(getTxtSearch());

        }
        return jContentPane;
    }

    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane();
            scrollPane.setBounds(10, 55, 881, 242);
            scrollPane.setViewportView(getTable());
            scrollPane.setBorder(
                new TitledBorder(null, "Topics", TitledBorder.LEADING, TitledBorder.TOP,
                    null, Color.BLUE));
        }
        return scrollPane;
    }

    private JTable getTable() {
        if (table == null) {
            table = new JTable();
        }
        return table;
    }

    private JButton getBtnProducer() {
        if (btnProducer == null) {
            btnProducer = new JButton("Producer");
            btnProducer.setBounds(0, 0, 139, 40);
        }
        return btnProducer;
    }

    private JButton getBtnConsumer() {
        if (btnConsumer == null) {
            btnConsumer = new JButton("Consumer");
            btnConsumer.setBounds(149, 0, 151, 40);
        }
        return btnConsumer;
    }

    private JButton getBtnClose() {
        if (btnClose == null) {
            btnClose = new JButton("Close");
            btnClose.setBounds(730, 0, 151, 40);
        }
        return btnClose;
    }

    private JButton getBtnCreateTopic() {
        if (btnCreateTopic == null) {
            btnCreateTopic = new JButton("Create Topic");
            btnCreateTopic.setBounds(310, 0, 139, 40);
        }
        return btnCreateTopic;
    }

    private JPanel getPanel() {
        if (panel == null) {
            panel = new JPanel();
            panel.setBounds(10, 307, 881, 41);
            panel.setLayout(null);
            panel.add(getBtnProducer());
            panel.add(getBtnConsumer());
            panel.add(getBtnCreateTopic());
            panel.add(getBtnClose());
            panel.add(getBtnDeleteTopic());
        }
        return panel;
    }

    private JTextField getTxtSearch() {
        if (txtSearch == null) {
            txtSearch = new JTextField();
            txtSearch.setBounds(10, 10, 388, 35);
            txtSearch.setColumns(10);
        }
        return txtSearch;
    }

    private JButton getBtnDeleteTopic() {
        if (btnDeleteTopic == null) {
            btnDeleteTopic = new JButton("Delete Topic");
            btnDeleteTopic.setBounds(459, 0, 139, 40);
        }
        return btnDeleteTopic;
    }
}

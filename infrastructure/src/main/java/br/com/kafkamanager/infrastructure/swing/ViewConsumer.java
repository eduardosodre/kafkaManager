package br.com.kafkamanager.infrastructure.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class ViewConsumer extends JDialog {

    private static final long serialVersionUID = 1L;
    private JPanel jContentPane = null;
    protected JScrollPane scrollPane;
    protected JTable table;
    protected JButton btnClose;
    protected JPanel panel;
    protected JLabel lblNewLabel;
    protected JComboBox<String> txtPartition;
    protected JLabel lblOffset;
    protected JTextField txtOffset;
    protected JLabel lbHeaders;
    protected JLabel lbMessage;

    public ViewConsumer(Window window) {
        super(window);
        initialize();
        setVisible(true);
    }

    private void initialize() {
        this.setSize(915, 543);
        this.setTitle("Kafka Consumer");
        this.setContentPane(getJContentPane());
        this.setResizable(false);
        this.setLocationRelativeTo(getOwner());
        this.setModal(true);
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getScrollPane());
            jContentPane.add(getPanel());
            jContentPane.add(getLblNewLabel());
            jContentPane.add(getTxtPartition());
            jContentPane.add(getLblOffset());
            jContentPane.add(getTxtOffset());
            jContentPane.add(getLbHeaders());
            jContentPane.add(getLbMessage());

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

    private JButton getBtnClose() {
        if (btnClose == null) {
            btnClose = new JButton("Close");
            btnClose.setBounds(730, 0, 151, 40);
        }
        return btnClose;
    }

    private JPanel getPanel() {
        if (panel == null) {
            panel = new JPanel();
            panel.setBounds(10, 455, 881, 41);
            panel.setLayout(null);
            panel.add(getBtnClose());
        }
        return panel;
    }

    private JLabel getLblNewLabel() {
        if (lblNewLabel == null) {
            lblNewLabel = new JLabel("Partition:");
            lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
            lblNewLabel.setBounds(10, 15, 102, 30);
        }
        return lblNewLabel;
    }

    private JComboBox<String> getTxtPartition() {
        if (txtPartition == null) {
            txtPartition = new JComboBox<>();
            txtPartition.setBounds(91, 15, 79, 30);
        }
        return txtPartition;
    }

    private JLabel getLblOffset() {
        if (lblOffset == null) {
            lblOffset = new JLabel("Offset:");
            lblOffset.setFont(new Font("Tahoma", Font.PLAIN, 14));
            lblOffset.setBounds(209, 15, 86, 30);
        }
        return lblOffset;
    }

    private JTextField getTxtOffset() {
        if (txtOffset == null) {
            txtOffset = new JTextField();
            txtOffset.setColumns(10);
            txtOffset.setBounds(278, 15, 96, 30);
            txtOffset.setText("0");
        }
        return txtOffset;
    }

    private JLabel getLbHeaders() {
        if (lbHeaders == null) {
            lbHeaders = new JLabel("New label");
            lbHeaders.setBounds(10, 302, 881, 58);
            lbHeaders.setBorder(
                new TitledBorder(null, "Headers", TitledBorder.LEADING, TitledBorder.TOP,
                    null, Color.BLUE));
        }
        return lbHeaders;
    }

    private JLabel getLbMessage() {
        if (lbMessage == null) {
            lbMessage = new JLabel("New label");
            lbMessage.setBounds(10, 365, 881, 86);
            lbMessage.setBorder(
                new TitledBorder(null, "Message", TitledBorder.LEADING, TitledBorder.TOP,
                    null, Color.BLUE));
        }
        return lbMessage;
    }
}

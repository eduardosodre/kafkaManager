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

public class ViewProducer extends JDialog {

    private static final long serialVersionUID = 1L;
    private JPanel jContentPane = null;
    protected JScrollPane scrollPane;
    protected JTable table;
    protected JButton btnProduce;
    protected JButton btnClose;
    protected JLabel lbTopicShow;
    protected JComboBox<String> comboTopic;
    protected JLabel lbKey;
    protected JLabel lbValue;
    protected JTextField txtKey;
    protected JTextField txtValue;
    protected JButton btnSave;
    protected JButton btnLoadProducer;
    protected JButton btnPlus;
    protected JButton btnSubtract;
    protected JButton btnImportHeaderKafdrop;


    public ViewProducer(Window owner) {
        super(owner);
        initialize();
    }

    private void initialize() {
        this.setSize(664, 593);
        this.setTitle("Kafka Producer");
        this.setContentPane(getJContentPane());
        this.setModal(true);
        this.setResizable(false);
        this.setLocationRelativeTo(getOwner());
    }


    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getScrollPane());
            jContentPane.add(getBtnProduce());
            jContentPane.add(getBtnClose());
            jContentPane.add(getLbTopicShow());
            jContentPane.add(getComboTopic());
            jContentPane.add(getLbKey());
            jContentPane.add(getLbValue());
            jContentPane.add(getTxtKey());
            jContentPane.add(getTxtValue());
            jContentPane.add(getBtnSave());
            jContentPane.add(getBtnLoadProducer());
            jContentPane.add(getBtnPlus());
            jContentPane.add(getBtnSubtract());
            jContentPane.add(getBtnImportHeaderKafdrop());

        }
        return jContentPane;
    }

    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane();
            scrollPane.setBounds(10, 222, 630, 274);
            scrollPane.setViewportView(getTable());
            scrollPane.setBorder(
                new TitledBorder(null, "Header(s)", TitledBorder.LEADING, TitledBorder.TOP,
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

    private JButton getBtnProduce() {
        if (btnProduce == null) {
            btnProduce = new JButton("Produce to Topic");
            btnProduce.setBounds(10, 506, 139, 40);
        }
        return btnProduce;
    }

    private JButton getBtnClose() {
        if (btnClose == null) {
            btnClose = new JButton("Close");
            btnClose.setBounds(489, 506, 151, 40);
        }
        return btnClose;
    }

    private JLabel getLbTopicShow() {
        if (lbTopicShow == null) {
            lbTopicShow = new JLabel("Topic");
            lbTopicShow.setFont(new Font("Tahoma", Font.PLAIN, 14));
            lbTopicShow.setBounds(10, 10, 91, 25);
        }
        return lbTopicShow;
    }

    private JComboBox<String> getComboTopic() {
        if (comboTopic == null) {
            comboTopic = new JComboBox<>();
            comboTopic.setForeground(Color.BLUE);
            comboTopic.setFont(new Font("Tahoma", Font.PLAIN, 16));
            comboTopic.setBounds(10, 45, 630, 40);
        }
        return comboTopic;
    }

    private JLabel getLbKey() {
        if (lbKey == null) {
            lbKey = new JLabel("Key:");
            lbKey.setFont(new Font("Tahoma", Font.PLAIN, 14));
            lbKey.setBounds(10, 111, 91, 25);
        }
        return lbKey;
    }

    private JLabel getLbValue() {
        if (lbValue == null) {
            lbValue = new JLabel("Value");
            lbValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
            lbValue.setBounds(10, 147, 91, 25);
        }
        return lbValue;
    }

    private JTextField getTxtKey() {
        if (txtKey == null) {
            txtKey = new JTextField();
            txtKey.setBounds(77, 107, 505, 32);
            txtKey.setColumns(10);
        }
        return txtKey;
    }

    private JTextField getTxtValue() {
        if (txtValue == null) {
            txtValue = new JTextField();
            txtValue.setBounds(77, 143, 505, 32);
            txtValue.setColumns(10);
        }
        return txtValue;
    }

    private JButton getBtnSave() {
        if (btnSave == null) {
            btnSave = new JButton("Save Producer");
            btnSave.setBounds(159, 506, 139, 40);
        }
        return btnSave;
    }

    private JButton getBtnLoadProducer() {
        if (btnLoadProducer == null) {
            btnLoadProducer = new JButton("Load Producer");
            btnLoadProducer.setBounds(308, 506, 139, 40);
        }
        return btnLoadProducer;
    }

    private JButton getBtnPlus() {
        if (btnPlus == null) {
            btnPlus = new JButton("+");
            btnPlus.setBounds(10, 191, 52, 21);
        }
        return btnPlus;
    }

    private JButton getBtnSubtract() {
        if (btnSubtract == null) {
            btnSubtract = new JButton("-");
            btnSubtract.setBounds(62, 191, 52, 21);
        }
        return btnSubtract;
    }

    private JButton getBtnImportHeaderKafdrop() {
        if (btnImportHeaderKafdrop == null) {
            btnImportHeaderKafdrop = new JButton("Import Header from Kafdrop");
            btnImportHeaderKafdrop.setBounds(119, 191, 240, 21);
        }
        return btnImportHeaderKafdrop;
    }
}
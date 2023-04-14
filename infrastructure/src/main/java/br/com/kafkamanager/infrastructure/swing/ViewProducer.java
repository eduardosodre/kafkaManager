package br.com.kafkamanager.infrastructure.swing;

import static javax.swing.SwingConstants.TOP;

import br.com.kafkamanager.infrastructure.swing.util.BackgroundPanel;
import br.com.kafkamanager.infrastructure.swing.util.TransparentJPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

public class ViewProducer extends JDialog {

    private static final long serialVersionUID = 1L;
    private JPanel jContentPane = null;
    protected JScrollPane scrollPaneHeaders;
    protected JTable table;
    protected JButton btnProduce;
    protected JButton btnClose;
    protected JLabel lbTopicShow;
    protected JComboBox<String> comboTopic;
    protected JLabel lbKey;
    protected JTextField txtKey;
    protected RSyntaxTextArea txtValue;
    protected JButton btnSave;
    protected JButton btnLoadProducer;
    protected JButton btnPlus;
    protected JButton btnSubtract;
    protected JButton btnImportHeaderKafdrop;
    protected JButton btnFormatter;
    protected JPanel panel;
    protected JTabbedPane tabbedPane;
    protected JScrollPane scrollPaneValue;


    public ViewProducer(Window owner) {
        super(owner);
        initialize();
    }

    private void initialize() {
        this.setSize(664, 720);
        this.setTitle("Kafka Producer");
        this.setContentPane(getJContentPane());
        this.setModal(true);
        this.setResizable(false);
        this.setLocationRelativeTo(getOwner());
    }


    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new BackgroundPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getLbTopicShow());
            jContentPane.add(getComboTopic());
            jContentPane.add(getLbKey());
            jContentPane.add(getTxtKey());
            jContentPane.add(getBtnPlus());
            jContentPane.add(getBtnSubtract());
            jContentPane.add(getBtnImportHeaderKafdrop());
            jContentPane.add(getBtnFormatter());
            jContentPane.add(getPanel());
            jContentPane.add(getTabbedPane());

        }
        return jContentPane;
    }

    private JScrollPane getScrollPaneHeaders() {
        if (scrollPaneHeaders == null) {
            scrollPaneHeaders = new JScrollPane();
            scrollPaneHeaders.setViewportView(getTable());
        }
        return scrollPaneHeaders;
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
            btnProduce.setBounds(0, 0, 139, 40);
        }
        return btnProduce;
    }

    private JButton getBtnClose() {
        if (btnClose == null) {
            btnClose = new JButton("Close");
            btnClose.setBounds(479, 0, 151, 40);
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

    private JTextField getTxtKey() {
        if (txtKey == null) {
            txtKey = new JTextField();
            txtKey.setBounds(77, 107, 505, 32);
            txtKey.setColumns(10);
        }
        return txtKey;
    }

    private RSyntaxTextArea getTxtValue() {
        if (txtValue == null) {
            txtValue = new RSyntaxTextArea();
            txtValue.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON_WITH_COMMENTS);
            txtValue.setCodeFoldingEnabled(true);
        }
        return txtValue;
    }

    private JButton getBtnSave() {
        if (btnSave == null) {
            btnSave = new JButton("Save Producer");
            btnSave.setBounds(149, 0, 139, 40);
        }
        return btnSave;
    }

    private JButton getBtnLoadProducer() {
        if (btnLoadProducer == null) {
            btnLoadProducer = new JButton("Load Producer");
            btnLoadProducer.setBounds(298, 0, 139, 40);
        }
        return btnLoadProducer;
    }

    private JButton getBtnPlus() {
        if (btnPlus == null) {
            btnPlus = new JButton("+");
            btnPlus.setBounds(10, 157, 52, 21);
        }
        return btnPlus;
    }

    private JButton getBtnSubtract() {
        if (btnSubtract == null) {
            btnSubtract = new JButton("-");
            btnSubtract.setBounds(62, 157, 52, 21);
        }
        return btnSubtract;
    }

    private JButton getBtnImportHeaderKafdrop() {
        if (btnImportHeaderKafdrop == null) {
            btnImportHeaderKafdrop = new JButton("Import Header from Kafdrop");
            btnImportHeaderKafdrop.setBounds(119, 157, 240, 21);
        }
        return btnImportHeaderKafdrop;
    }

    private JButton getBtnFormatter() {
        if (btnFormatter == null) {
            btnFormatter = new JButton("Formatter value");
            btnFormatter.setBounds(363, 157, 240, 21);
        }
        return btnFormatter;
    }

    private JPanel getPanel() {
        if (panel == null) {
            panel = new TransparentJPanel();
            panel.setBounds(10, 633, 630, 40);
            panel.setLayout(null);
            panel.add(getBtnProduce());
            panel.add(getBtnSave());
            panel.add(getBtnLoadProducer());
            panel.add(getBtnClose());
        }
        return panel;
    }

    private JTabbedPane getTabbedPane() {
        if (tabbedPane == null) {
            tabbedPane = new JTabbedPane(TOP);
            tabbedPane.setBounds(10, 188, 630, 435);
            tabbedPane.addTab("Value", null, getScrollPaneValue(), null);
            tabbedPane.addTab("Headers", null, getScrollPaneHeaders(), null);
        }
        return tabbedPane;
    }

    private JScrollPane getScrollPaneValue() {
        if (scrollPaneValue == null) {
            scrollPaneValue = new JScrollPane();
            scrollPaneValue.setViewportView(getTxtValue());
        }
        return scrollPaneValue;
    }
}
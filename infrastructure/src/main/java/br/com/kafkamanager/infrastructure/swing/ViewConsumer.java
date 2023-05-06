package br.com.kafkamanager.infrastructure.swing;

import static javax.swing.SwingConstants.TOP;

import br.com.kafkamanager.infrastructure.swing.util.TransparentJPanel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

public class ViewConsumer extends JFrame {

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
    protected JScrollPane scrollHeader;
    protected JScrollPane scrollMessage;
    protected JTextPane txtHeaders;
    protected RSyntaxTextArea txtMessage;
    protected JLabel lbOffset;
    protected JTabbedPane tabbedPane;

    public ViewConsumer() {
        super();
        initialize();
    }

    private void initialize() {
        this.setSize(965, 685);
        this.setTitle("Kafka Consumer");
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
            jContentPane.add(getLblNewLabel());
            jContentPane.add(getTxtPartition());
            jContentPane.add(getLblOffset());
            jContentPane.add(getTxtOffset());
            jContentPane.add(getLbOffset());
            jContentPane.add(getTabbedPane());
        }
        return jContentPane;
    }

    private JTabbedPane getTabbedPane() {
        if (tabbedPane == null) {
            tabbedPane = new JTabbedPane(TOP);
            tabbedPane.setBounds(10, 302, 931, 300);
            tabbedPane.setFont(UIManager.getFont( "h4.font" ));
            tabbedPane.addTab("Message", null, getScrollMessage(), null);
            tabbedPane.addTab("Headers", null, getScrollHeader(), null);
        }
        return tabbedPane;
    }

    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane();
            scrollPane.setBounds(10, 55, 931, 242);
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

    private JButton getBtnClose() {
        if (btnClose == null) {
            btnClose = new JButton("Close");
            btnClose.setBounds(780, 0, 151, 25);
        }
        return btnClose;
    }

    private JPanel getPanel() {
        if (panel == null) {
            panel = new TransparentJPanel();
            panel.setBounds(10, 610, 931, 25);
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

    private JLabel getLbOffset() {
        if (lbOffset == null) {
            lbOffset = new JLabel();
            lbOffset.setBounds(380, 15, 200, 30);
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
            txtMessage.setBackground(UIManager.getColor( "TextArea.background" ));
            txtMessage.setFont(UIManager.getFont("TextArea.font"));
            txtMessage.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON_WITH_COMMENTS);
            txtMessage.setCodeFoldingEnabled(true);
        }
        return txtMessage;
    }
}

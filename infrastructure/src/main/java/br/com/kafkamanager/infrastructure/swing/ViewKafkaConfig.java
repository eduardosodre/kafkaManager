package br.com.kafkamanager.infrastructure.swing;

import br.com.kafkamanager.infrastructure.swing.util.BackgroundPanel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ViewKafkaConfig extends JDialog {

    private static final long serialVersionUID = 1L;
    private JPanel jContentPane = null;
    private JLabel lblBootstrapServers;
    protected JTextField txtServer;
    protected JButton btnSave;
    protected JButton btnCancel;

    public ViewKafkaConfig() {
        super();
        initialize();
    }

    private void initialize() {
        this.setSize(460, 152);
        this.setTitle("Kafka Config");
        this.setContentPane(getJContentPane());
        this.setModal(true);
        this.setResizable(false);
        this.setLocationRelativeTo(getOwner());
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new BackgroundPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getLblBootstrapServers());
            jContentPane.add(getTxtServer());
            jContentPane.add(getBtnSave());
            jContentPane.add(getBtnCancel());

        }
        return jContentPane;
    }

    private JLabel getLblBootstrapServers() {
        if (lblBootstrapServers == null) {
            lblBootstrapServers = new JLabel("Bootstrap Servers:");
            lblBootstrapServers.setBounds(10, 21, 113, 25);
        }
        return lblBootstrapServers;
    }

    private JTextField getTxtServer() {
        if (txtServer == null) {
            txtServer = new JTextField();
            txtServer.setColumns(10);
            txtServer.setBounds(127, 21, 277, 25);
            txtServer.setText("localhost:9092");
        }
        return txtServer;
    }

    private JButton getBtnSave() {
        if (btnSave == null) {
            btnSave = new JButton("Save");
            btnSave.setBounds(79, 56, 131, 36);
        }
        return btnSave;
    }

    private JButton getBtnCancel() {
        if (btnCancel == null) {
            btnCancel = new JButton("Cancel");
            btnCancel.setBounds(233, 56, 131, 36);
        }
        return btnCancel;
    }
}

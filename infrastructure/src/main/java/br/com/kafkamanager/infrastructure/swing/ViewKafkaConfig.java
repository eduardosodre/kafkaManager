package br.com.kafkamanager.infrastructure.swing;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class ViewKafkaConfig extends JDialog {

    private static final long serialVersionUID = 1L;
    private JPanel jContentPane = null;
    private JLabel lblBootstrapServers;
    protected JTextField txtServer;
    protected JButton btnSave;
    protected JButton btnCancel;
    private JPanel panelLogo;
    protected JLabel lbLogo;
    protected JLabel lbLogoIcon;
    private JSeparator separator;

    public ViewKafkaConfig() {
        super();
        initialize();
    }

    private void initialize() {
        this.setSize(430, 193);
        this.setTitle("Kafka Config");
        this.setContentPane(getJContentPane());
        this.setModal(true);
        this.setResizable(false);
        this.setLocationRelativeTo(getOwner());
    	getJContentPane().add(getPanelLogo());
    	getJContentPane().add(getSeparator());
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
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
            lblBootstrapServers.setBounds(10, 78, 113, 25);
        }
        return lblBootstrapServers;
    }

    private JTextField getTxtServer() {
        if (txtServer == null) {
            txtServer = new JTextField();
            txtServer.setColumns(10);
            txtServer.setBounds(127, 78, 277, 25);
            txtServer.setText("localhost:9092");
        }
        return txtServer;
    }

    private JButton getBtnSave() {
        if (btnSave == null) {
            btnSave = new JButton("Save");
            btnSave.setBounds(65, 120, 131, 25);
        }
        return btnSave;
    }

    private JButton getBtnCancel() {
        if (btnCancel == null) {
            btnCancel = new JButton("Cancel");
            btnCancel.setBounds(219, 120, 131, 25);
        }
        return btnCancel;
    }
	private JPanel getPanelLogo() {
		if (panelLogo == null) {
			panelLogo = new JPanel();
			panelLogo.setLayout(null);
			panelLogo.setBounds(127, 11, 223, 35);
			panelLogo.add(getLbLogo());
			panelLogo.add(getLbLogoIcon());
		}
		return panelLogo;
	}
	private JLabel getLbLogo() {
		if (lbLogo == null) {
			lbLogo = new JLabel("KAFKA MANAGER");
			lbLogo.setHorizontalAlignment(SwingConstants.CENTER);
			lbLogo.setFont(UIManager.getFont("h3.font"));
			lbLogo.setBounds(48, 0, 123, 35);
		}
		return lbLogo;
	}
	private JLabel getLbLogoIcon() {
		if (lbLogoIcon == null) {
			lbLogoIcon = new JLabel();
			lbLogoIcon.setHorizontalAlignment(SwingConstants.LEFT);
			lbLogoIcon.setBounds(0, 0, 46, 35);
		}
		return lbLogoIcon;
	}
	private JSeparator getSeparator() {
		if (separator == null) {
			separator = new JSeparator();
			separator.setBounds(10, 59, 395, 2);
		}
		return separator;
	}
}

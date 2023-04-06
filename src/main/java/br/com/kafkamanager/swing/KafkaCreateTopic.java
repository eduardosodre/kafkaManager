package br.com.kafkamanager.swing;

import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class KafkaCreateTopic extends JDialog {

    private static final long serialVersionUID = 1L;
    protected JPanel jContentPane = null;
    protected JLabel lbTopicName;
    protected JTextField txtTopicName;
    protected JButton btnSave;
    protected JButton btnCancel;
    protected JLabel lblPartitions;
    protected JLabel lbReplications;
    protected JTextField txtPartitions;
    protected JTextField txtReplications;

    public KafkaCreateTopic(Window owner) {
        super(owner);
        initialize();
    }

    private void initialize() {
        this.setSize(460, 318);
        this.setTitle("Kafka Create Topic");
        this.setContentPane(getJContentPane());
        this.setModal(true);
        this.setResizable(false);
        this.setLocationRelativeTo(getOwner());
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getLbTopicName());
            jContentPane.add(getTxtTopicName());
            jContentPane.add(getBtnSave());
            jContentPane.add(getBtnCancel());
            jContentPane.add(getLblPartitions());
            jContentPane.add(getLbReplications());
            jContentPane.add(getTxtPartitions());
            jContentPane.add(getTxtReplications());

        }
        return jContentPane;
    }

    private JLabel getLbTopicName() {
        if (lbTopicName == null) {
            lbTopicName = new JLabel("Topic Name:");
            lbTopicName.setBounds(10, 21, 113, 25);
        }
        return lbTopicName;
    }

    private JTextField getTxtTopicName() {
        if (txtTopicName == null) {
            txtTopicName = new JTextField();
            txtTopicName.setColumns(10);
            txtTopicName.setBounds(127, 21, 277, 25);
        }
        return txtTopicName;
    }

    private JButton getBtnSave() {
        if (btnSave == null) {
            btnSave = new JButton("Save");
            btnSave.setBounds(84, 219, 131, 36);
        }
        return btnSave;
    }

    private JButton getBtnCancel() {
        if (btnCancel == null) {
            btnCancel = new JButton("Cancel");
            btnCancel.setBounds(238, 219, 131, 36);
        }
        return btnCancel;
    }
	private JLabel getLblPartitions() {
		if (lblPartitions == null) {
			lblPartitions = new JLabel("Partitions:");
			lblPartitions.setBounds(10, 64, 113, 25);
		}
		return lblPartitions;
	}
	private JLabel getLbReplications() {
		if (lbReplications == null) {
			lbReplications = new JLabel("Replications:");
			lbReplications.setBounds(10, 113, 113, 25);
		}
		return lbReplications;
	}
	private JTextField getTxtPartitions() {
		if (txtPartitions == null) {
			txtPartitions = new JTextField();
			txtPartitions.setText("1");
			txtPartitions.setColumns(10);
			txtPartitions.setBounds(127, 67, 277, 25);
		}
		return txtPartitions;
	}
	private JTextField getTxtReplications() {
		if (txtReplications == null) {
			txtReplications = new JTextField();
			txtReplications.setText("1");
			txtReplications.setColumns(10);
			txtReplications.setBounds(127, 116, 277, 25);
		}
		return txtReplications;
	}
}

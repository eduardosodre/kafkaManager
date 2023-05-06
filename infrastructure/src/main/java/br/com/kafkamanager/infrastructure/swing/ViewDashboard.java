package br.com.kafkamanager.infrastructure.swing;

import br.com.kafkamanager.infrastructure.swing.util.MonochromeTableCellRenderer;
import br.com.kafkamanager.infrastructure.swing.util.TransparentJPanel;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.formdev.flatlaf.ui.FlatUIUtils;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

public class ViewDashboard extends JFrame {

    private static final long serialVersionUID = 1L;
    protected JPanel jContentPane = null;
    protected JScrollPane scrollPane;
    protected JTable table;
    protected JButton btnProducer;
    protected JButton btnConsumer;
    protected JButton btnClose;
    protected JButton btnCreateTopic;
    protected JPanel panelHeaderOptions;
    protected FlatTextField txtSearch;
    protected JButton lbIconTxtSearch;
    protected JButton btnDeleteTopic;
    protected JPanel panelMenu;
    protected JPanel panelBody;
    protected JPanel panelHeader;
    protected JPanel panelHeaderLogo;
    protected JLabel lbLogo;
    protected JLabel lbLogoIcon;
    protected JComboBox<String> comboTheme;
    protected JPanel panelFooter;
    protected JPanel panelMenuItemTopic;
    protected JLabel lbInfoConnection;
    protected JLabel lbThemes;
    protected JLabel lbMenuItemTopicIcon;
    protected JLabel lbMenuItemName;
    protected JButton btnUpdate;

    public ViewDashboard() {
        super();
        initialize();
        setVisible(true);
    }

    private void initialize() {
        this.setSize(915, 600);
        this.setTitle("Kafka Dashboard");
        this.setContentPane(getJContentPane());
        this.setResizable(false);
        this.setLocationRelativeTo(getOwner());
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getPanelBody());
            jContentPane.add(getPanelMenu());
            jContentPane.add(getPanelHeader());
            jContentPane.add(getPanelFooter());

        }
        return jContentPane;
    }

    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane();
            scrollPane.setBounds(10, 44, 669, 405);
            scrollPane.setViewportView(getTable());
        }
        return scrollPane;
    }

    private JTable getTable() {
        if (table == null) {
            table = new JTable();
			table.setDefaultRenderer(Object.class, new MonochromeTableCellRenderer());
        }
        return table;
    }

    private JButton getBtnProducer() {
        if (btnProducer == null) {
            btnProducer = new JButton("Producer");
            btnProducer.setBounds(142, 5, 140, 25);
        } 
        return btnProducer;
    }

    private JButton getBtnConsumer() {
        if (btnConsumer == null) {
            btnConsumer = new JButton("Consumer");
            btnConsumer.setBounds(287, 5, 140, 25);
        }
        return btnConsumer;
    }

    private JButton getBtnClose() {
        if (btnClose == null) {
            btnClose = new JButton();
            btnClose.setBounds(644, 5, 35, 25);
        }
        return btnClose;
    }

    private JButton getBtnCreateTopic() {
        if (btnCreateTopic == null) {
            btnCreateTopic = new JButton("Create Topic");
            btnCreateTopic.setBounds(396, 458, 139, 25);
        }
        return btnCreateTopic;
    }

    private JPanel getPanelHeaderOptions() {
        if (panelHeaderOptions == null) {
            panelHeaderOptions = new TransparentJPanel();
            panelHeaderOptions.setBounds(210, 5, 679, 35);
            panelHeaderOptions.setLayout(null);
            panelHeaderOptions.add(getBtnProducer());
            panelHeaderOptions.add(getBtnConsumer());
            panelHeaderOptions.add(getBtnClose());
            panelHeaderOptions.add(getComboTheme());
            panelHeaderOptions.add(getLbThemes());
            panelHeaderOptions.add(getBtnUpdate());
        }
        return panelHeaderOptions;
    }

    private JTextField getTxtSearch() {
        if (txtSearch == null) {
            txtSearch = new FlatTextField();
            txtSearch.setBounds(281, 10, 398, 25);
            txtSearch.setPlaceholderText("Search");
        }
        return txtSearch;
    }

    private JButton getBtnDeleteTopic() {
        if (btnDeleteTopic == null) {
            btnDeleteTopic = new JButton("Delete Topic");
            btnDeleteTopic.setBounds(540, 458, 139, 25);
        }
        return btnDeleteTopic;
    }

	private JPanel getPanelMenu() {
		if (panelMenu == null) {
			panelMenu = new JPanel();
			panelMenu.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			panelMenu.setBounds(0, 46, 200, 494);
			panelMenu.setLayout(null);
			panelMenu.add(getPanelMenuItemTopic());
		}
		return panelMenu;
	}

	private JPanel getPanelBody() {
		if (panelBody == null) {
			panelBody = new JPanel();
			panelBody.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			panelBody.setBounds(210, 46, 689, 494);
			panelBody.setLayout(null);
			panelBody.add(getScrollPane());
			panelBody.add(getTxtSearch());
			panelBody.add(getBtnCreateTopic());
			panelBody.add(getBtnDeleteTopic());
		}
		return panelBody;
	}

	private JPanel getPanelHeader() {
		if (panelHeader == null) {
			panelHeader = new JPanel();
			panelHeader.setOpaque(false);
			panelHeader.setBounds(0, 0, 899, 41);
			panelHeader.setLayout(null);
			panelHeader.add(getPanelHeaderLogo());
			panelHeader.add(getPanelHeaderOptions());
		}
		return panelHeader;
	}

	private JPanel getPanelHeaderLogo() {
		if (panelHeaderLogo == null) {
			panelHeaderLogo = new JPanel();
			panelHeaderLogo.setOpaque(false);
			panelHeaderLogo.setBounds(10, 5, 190, 35);
			panelHeaderLogo.setLayout(null);
			panelHeaderLogo.add(getLbLogo());
			panelHeaderLogo.add(getLbLogoIcon());
		}
		return panelHeaderLogo;
	}

	private JLabel getLbLogo() {
		if (lbLogo == null) {
			lbLogo = new JLabel("KAFKA MANAGER");
			lbLogo.setHorizontalAlignment(SwingConstants.LEFT);
			lbLogo.setFont(FlatUIUtils.nonUIResource(UIManager.getFont("h2.font")));
			lbLogo.setBounds(35, 0, 156, 35);
		}
		return lbLogo;
	}

	private JComboBox<String> getComboTheme() {
		if (comboTheme == null) {
			comboTheme = new JComboBox<>();
			comboTheme.setBounds(530, 5, 110, 25);
		}
		return comboTheme;
	}

	private JPanel getPanelFooter() {
		if (panelFooter == null) {
			panelFooter = new JPanel();
			panelFooter.setOpaque(false);
			panelFooter.setBounds(0, 540, 899, 21);
			panelFooter.setLayout(null);
			panelFooter.add(getLbInfoConnection());
		}
		return panelFooter;
	}

	private JPanel getPanelMenuItemTopic() {
		if (panelMenuItemTopic == null) {
			panelMenuItemTopic = new JPanel();
			panelMenuItemTopic.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			panelMenuItemTopic.setBounds(0, 0, 200, 40);
			panelMenuItemTopic.setLayout(null);
			panelMenuItemTopic.add(getLbMenuItemTopicIcon());
			panelMenuItemTopic.add(getLbMenuItemName());
		}
		return panelMenuItemTopic;
	}

	private JLabel getLbInfoConnection() {
		if (lbInfoConnection == null) {
			lbInfoConnection = new JLabel("localhost : 9092");
			lbInfoConnection.setBounds(5, 0, 200, 16);
		}
		return lbInfoConnection;
	}

	private JLabel getLbThemes() {
		if (lbThemes == null) {
			lbThemes = new JLabel("Themes:");
			lbThemes.setBounds(477, 5, 52, 25);
		}
		return lbThemes;
	}

	private JLabel getLbMenuItemTopicIcon() {
		if (lbMenuItemTopicIcon == null) {
			lbMenuItemTopicIcon = new JLabel();
			lbMenuItemTopicIcon.setBounds(10, 10, 30, 20);
		}
		return lbMenuItemTopicIcon;
	}

	private JLabel getLbMenuItemName() {
		if (lbMenuItemName == null) {
			lbMenuItemName = new JLabel("Topics");
			lbMenuItemName.setFont(FlatUIUtils.nonUIResource(UIManager.getFont("h3.font")));
			lbMenuItemName.setBounds(45, 10, 140, 20);
		}
		return lbMenuItemName;
	}

	private JLabel getLbLogoIcon() {
		if (lbLogoIcon == null) {
			lbLogoIcon = new JLabel();
			lbLogoIcon.setBounds(0, 0, 30, 35);
		}
		return lbLogoIcon;
	}

	private JButton getBtnUpdate() {
		if (btnUpdate == null) {
			btnUpdate = new JButton();
			btnUpdate.setOpaque(false);
			btnUpdate.setBounds(432, 5, 35, 25);
		}
		return btnUpdate;
	}
}

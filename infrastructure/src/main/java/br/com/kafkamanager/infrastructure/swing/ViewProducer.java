package br.com.kafkamanager.infrastructure.swing;

import static javax.swing.SwingConstants.TOP;

import com.formdev.flatlaf.extras.components.FlatTabbedPane;
import com.formdev.flatlaf.extras.components.FlatTextField;
import com.formdev.flatlaf.ui.FlatUIUtils;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

public class ViewProducer extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel jContentPane = null;
    protected JScrollPane scrollPaneHeaders;
    protected JTable tableHeader;
    protected JButton btnProduce;
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
    protected JTabbedPane tabbedPane;
    protected JScrollPane scrollPaneValue;
    protected JPanel panelData;
    protected JPanel panelListProducer;
    protected JPanel panelSearch;
    protected JPanel panelListProducerControl;
    protected JPanel panelHeaders;
    protected JPanel panelListProducerSent;
    protected JPanel panelButtonProducer;
    protected JScrollPane scrollPaneListProducer;
    protected FlatTextField txtSearch;
    protected JButton btnNewProducer;
    protected JScrollPane scrollPaneListProduced;
    protected JPanel panelDataHeader;
    protected JTabbedPane tabbedPaneDataValue;
    protected JTable tableListProducer;
    protected JButton btnDeleteProducer;
    protected JTable tableSentProducers;


    public ViewProducer() {
        super();
        initialize();
    }

    private void initialize() {
        this.setSize(1000, 600);
        this.setTitle("Kafka Producer");
        this.setContentPane(getJContentPane());
        this.setResizable(false);
        this.setLocationRelativeTo(getOwner());
    	getJContentPane().add(getPanelListProducer());
    	getJContentPane().add(getPanelListProducerSent());
    }


    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getTabbedPane());

        }
        return jContentPane;
    }

    private JScrollPane getScrollPaneHeaders() {
        if (scrollPaneHeaders == null) {
            scrollPaneHeaders = new JScrollPane();
            scrollPaneHeaders.setBounds(0, 43, 563, 485);
            scrollPaneHeaders.setViewportView(getTableHeader());
        }
        return scrollPaneHeaders;
    }

    private JTable getTableHeader() {
        if (tableHeader == null) {
            tableHeader = new JTable();
        }
        return tableHeader;
    }

    private JButton getBtnProduce() {
        if (btnProduce == null) {
            btnProduce = new JButton("Produce to Topic");
            btnProduce.setBounds(0, 0, 200, 25);
        }
        return btnProduce;
    }

    private JLabel getLbTopicShow() {
        if (lbTopicShow == null) {
            lbTopicShow = new JLabel("Topic:");
            lbTopicShow.setBounds(10, 11, 68, 25);
        }
        return lbTopicShow;
    }

    private JComboBox<String> getComboTopic() {
        if (comboTopic == null) {
            comboTopic = new JComboBox<>();
            comboTopic.setBounds(77, 13, 395, 25);
        }
        return comboTopic;
    }

    private JLabel getLbKey() {
        if (lbKey == null) {
            lbKey = new JLabel("Key:");
            lbKey.setBounds(10, 46, 68, 25);
        }
        return lbKey;
    }

    private JTextField getTxtKey() {
        if (txtKey == null) {
            txtKey = new JTextField();
            txtKey.setBounds(77, 47, 395, 25);
        }
        return txtKey;
    }

    private RSyntaxTextArea getTxtValue() {
        if (txtValue == null) {
            txtValue = new RSyntaxTextArea();
            txtValue.setBackground(UIManager.getColor( "TextArea.background" ));
            txtValue.setFont(UIManager.getFont("TextArea.font"));
            txtValue.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON_WITH_COMMENTS);
            txtValue.setCodeFoldingEnabled(true);
        }
        return txtValue;
    }

    private JButton getBtnSave() {
        if (btnSave == null) {
            btnSave = new JButton();
            btnSave.setBounds(154, 0, 46, 25);
        }
        return btnSave;
    }

    private JButton getBtnLoadProducer() {
        if (btnLoadProducer == null) {
            btnLoadProducer = new JButton();
            btnLoadProducer.setBounds(0, 0, 45, 25);
        }
        return btnLoadProducer;
    }

    private JButton getBtnPlus() {
        if (btnPlus == null) {
            btnPlus = new JButton();
            btnPlus.setBounds(0, 10, 35, 25);
        }
        return btnPlus;
    }

    private JButton getBtnSubtract() {
        if (btnSubtract == null) {
            btnSubtract = new JButton();
            btnSubtract.setBounds(41, 10, 35, 25);
        }
        return btnSubtract;
    }

    private JButton getBtnImportHeaderKafdrop() {
        if (btnImportHeaderKafdrop == null) {
            btnImportHeaderKafdrop = new JButton("Import Header from Kafdrop");
            btnImportHeaderKafdrop.setBounds(83, 10, 273, 25);
        }
        return btnImportHeaderKafdrop;
    }

    private JButton getBtnFormatter() {
        if (btnFormatter == null) {
            btnFormatter = new JButton();
        }
        return btnFormatter;
    }

    private JTabbedPane getTabbedPane() {
        if (tabbedPane == null) {
            tabbedPane = new JTabbedPane(TOP);
            tabbedPane.setVisible(false);
            tabbedPane.setBounds(209, 0, 566, 557);
            tabbedPane.addTab("Data", null, getPanelData(), null);
            tabbedPane.addTab("Headers", null, getPanelHeaders(), null);
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
    
	private JPanel getPanelData() {
		if (panelData == null) {
			panelData = new JPanel();
			panelData.setLayout(null);
			panelData.add(getPanelDataHeader());
			panelData.add(getTabbedPaneDataValue());
		}
		return panelData;
	}
	
	private JPanel getPanelListProducer() {
		if (panelListProducer == null) {
			panelListProducer = new JPanel();
			panelListProducer.setBounds(0, 0, 200, 561);
			panelListProducer.setLayout(null);
			panelListProducer.add(getPanelSearch());
			panelListProducer.add(getPanelListProducerControl());
			panelListProducer.add(getScrollPaneListProducer());
		}
		return panelListProducer;
	}
	
	private JPanel getPanelSearch() {
		if (panelSearch == null) {
			panelSearch = new JPanel();
			panelSearch.setBounds(0, 0, 200, 34);
			panelSearch.setLayout(null);
			panelSearch.add(getTxtSearch());
		}
		return panelSearch;
	}
	
	private JPanel getPanelListProducerControl() {
		if (panelListProducerControl == null) {
			panelListProducerControl = new JPanel();
			panelListProducerControl.setBounds(0, 530, 200, 25);
			panelListProducerControl.setLayout(null);
			panelListProducerControl.add(getBtnSave());
			panelListProducerControl.add(getBtnLoadProducer());
			panelListProducerControl.add(getBtnNewProducer());
			panelListProducerControl.add(getBtnDeleteProducer());
		}
		return panelListProducerControl;
	}
	
	private JPanel getPanelHeaders() {
		if (panelHeaders == null) {
			panelHeaders = new JPanel();
			panelHeaders.setLayout(null);
			panelHeaders.add(getScrollPaneHeaders());
			panelHeaders.add(getBtnPlus());
			panelHeaders.add(getBtnSubtract());
			panelHeaders.add(getBtnImportHeaderKafdrop());
		}
		return panelHeaders;
	}
	
	private JPanel getPanelListProducerSent() {
		if (panelListProducerSent == null) {
			panelListProducerSent = new JPanel();
			panelListProducerSent.setBounds(784, 0, 200, 561);
			panelListProducerSent.setLayout(null);
			panelListProducerSent.add(getPanelButtonProducer());
			panelListProducerSent.add(getScrollPaneListProduced());
		}
		return panelListProducerSent;
	}
	
	private JPanel getPanelButtonProducer() {
		if (panelButtonProducer == null) {
			panelButtonProducer = new JPanel();
			panelButtonProducer.setBounds(0, 530, 200, 25);
			panelButtonProducer.setLayout(null);
			panelButtonProducer.add(getBtnProduce());
		}
		return panelButtonProducer;
	}
	
	private JScrollPane getScrollPaneListProducer() {
		if (scrollPaneListProducer == null) {
			scrollPaneListProducer = new JScrollPane();
			scrollPaneListProducer.setBounds(0, 34, 200, 491);
			scrollPaneListProducer.setViewportView(getTableListProducer());
		}
		return scrollPaneListProducer;
	}
	
	private JTextField getTxtSearch() {
		if (txtSearch == null) {
			txtSearch = new FlatTextField();
			txtSearch.setBounds(0, 0, 200, 34);
			txtSearch.setPlaceholderText("Search");
		}
		return txtSearch;
	}
	
	private JButton getBtnNewProducer() {
		if (btnNewProducer == null) {
			btnNewProducer = new JButton();
			btnNewProducer.setBounds(51, 0, 45, 25);
		}
		return btnNewProducer;
	}
	
	private JScrollPane getScrollPaneListProduced() {
		if (scrollPaneListProduced == null) {
			scrollPaneListProduced = new JScrollPane();
			scrollPaneListProduced.setBounds(0, 0, 200, 525);
			scrollPaneListProduced.setViewportView(getTableSentProducers());
		}
		return scrollPaneListProduced;
	}
	
	private JPanel getPanelDataHeader() {
		if (panelDataHeader == null) {
			panelDataHeader = new JPanel();
			panelDataHeader.setBounds(0, 0, 563, 83);
			panelDataHeader.setLayout(null);
			panelDataHeader.add(getLbTopicShow());
			panelDataHeader.add(getLbKey());
			panelDataHeader.add(getTxtKey());
			panelDataHeader.add(getComboTopic());
		}
		return panelDataHeader;
	}
	
	private JTabbedPane getTabbedPaneDataValue() {
		if (tabbedPaneDataValue == null) {
			tabbedPaneDataValue = new FlatTabbedPane();
			tabbedPaneDataValue.setBounds(0, 82, 566, 449);
			tabbedPaneDataValue.addTab(null, getScrollPaneValue());
			
			JLabel lbTab = new JLabel("Value ");
			lbTab.setOpaque(false);
			lbTab.setFont(FlatUIUtils.nonUIResource(UIManager.getFont("ToolBar.font")));
			
			JPanel panelTabBtn = new JPanel();
			panelTabBtn.setOpaque(false);
			panelTabBtn.add(lbTab);
			panelTabBtn.add(getBtnFormatter());
			tabbedPaneDataValue.setTabComponentAt(tabbedPaneDataValue.getTabCount()-1, panelTabBtn);
		}
		return tabbedPaneDataValue;
	}
	
	private JTable getTableListProducer() {
		if (tableListProducer == null) {
			tableListProducer = new JTable();
		}
		return tableListProducer;
	}
	
	private JButton getBtnDeleteProducer() {
		if (btnDeleteProducer == null) {
			btnDeleteProducer = new JButton();
			btnDeleteProducer.setBounds(102, 0, 46, 25);
		}
		return btnDeleteProducer;
	}
	private JTable getTableSentProducers() {
		if (tableSentProducers == null) {
			tableSentProducers = new JTable();
		}
		return tableSentProducers;
	}
}
package br.com.kafkamanager.infrastructure.swing;

import br.com.kafkamanager.infrastructure.swing.util.SetupColor;
import com.formdev.flatlaf.extras.FlatSVGIcon;

public class ViewKafkaConfigController extends ViewKafkaConfig {

    private String server;

    public ViewKafkaConfigController(String kafkaServerUrl) {
        start();
        txtServer.setText(kafkaServerUrl);
        setVisible(true);
    }

    private void start() {
        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> cancel());
    	setupIcons();
    }

    private void save() {
        server = txtServer.getText();
        dispose();
    }

    private void cancel() {
        server = null;
        dispose();
    }

    public String getServer() {
        return server;
    }
    
	private void setupIcons() {
		var iconLbIcon = new FlatSVGIcon("icons/apache_kafka-icon.svg", 30, 30, getClass().getClassLoader());
		iconLbIcon.setColorFilter(SetupColor.getIconColor());
		lbLogoIcon.setIcon(iconLbIcon);
		
		var iconBtnSave = new FlatSVGIcon("icons/login_box_line_icon.svg", 20, 20, getClass().getClassLoader());
		iconBtnSave.setColorFilter(SetupColor.getIconColor());
		btnSave.setIcon(iconBtnSave);
		
		var iconBtnCancel = new FlatSVGIcon("icons/close_outline_icon.svg", 18, 18, getClass().getClassLoader());
		iconBtnCancel.setColorFilter(SetupColor.getIconColor());
		btnCancel.setIcon(iconBtnCancel);
	}
}

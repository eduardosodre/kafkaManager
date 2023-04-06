package br.com.kafkamanager.swing;

import java.awt.Window;

public class KafkaConfigController extends KafkaConfig {

    private String host;

    public KafkaConfigController(Window owner) {
        super(owner);
        start();
        setVisible(true);
    }

    private void start() {
        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> cancel());
    }

    private void save() {
        host = txtServer.getText();
        dispose();
    }

    private void cancel() {
        host = null;
        dispose();
    }

    public String getHost() {
        return host;
    }
}

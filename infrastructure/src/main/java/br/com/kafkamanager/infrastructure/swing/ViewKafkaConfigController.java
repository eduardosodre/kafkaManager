package br.com.kafkamanager.infrastructure.swing;

public class ViewKafkaConfigController extends ViewKafkaConfig {

    private String server;

    public ViewKafkaConfigController() {
        start();
        setVisible(true);
    }

    private void start() {
        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> cancel());
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
}

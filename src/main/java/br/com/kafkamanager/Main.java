package br.com.kafkamanager;

import br.com.kafkamanager.swing.KafkaDashboardController;
import br.com.kafkamanager.util.LookAndFeelUtil;

public class Main {

    public static void main(String[] args) {
        LookAndFeelUtil.startLookAndFeel();
        new KafkaDashboardController();
    }
}

package br.com.kafkamanager.infrastructure;

import br.com.kafkamanager.infrastructure.preference.UserPreferenceService;
import br.com.kafkamanager.infrastructure.swing.ViewDashboardController;
import br.com.kafkamanager.infrastructure.swing.ViewKafkaConfigController;
import br.com.kafkamanager.infrastructure.swing.util.LookAndFeelUtil;
import br.com.kafkamanager.infrastructure.util.ContextUtil;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MapPropertySource;

public class Main {

    public static void main(String[] args) {
        final var preferenceDto = UserPreferenceService.getPreferences();
        LookAndFeelUtil.startLookAndFeel(preferenceDto.getThemeName());

        final var server = new ViewKafkaConfigController(preferenceDto.getKafkaServerUrl()).getServer();

        Map<String, Object> props = new HashMap<>();
        props.put("KAFKA_SERVER", server);

        System.setProperty("KAFKA_SERVER", server);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
        context.getEnvironment().getPropertySources()
            .addFirst(new MapPropertySource("myProps", props));

        ContextUtil.setContext(context);

        new ViewDashboardController();
    }
}

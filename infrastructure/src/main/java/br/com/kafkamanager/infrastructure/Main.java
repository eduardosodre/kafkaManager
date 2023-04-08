package br.com.kafkamanager.infrastructure;

import br.com.kafkamanager.infrastructure.swing.ViewDashboardController;
import br.com.kafkamanager.infrastructure.swing.ViewKafkaConfigController;
import br.com.kafkamanager.infrastructure.util.ContextUtil;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MapPropertySource;

public class Main {

    public static void main(String[] args) {
        final var server = new ViewKafkaConfigController().getServer();

        Map<String, Object> props = new HashMap<>();
        props.put("KAFKA_SERVER", server);

        System.setProperty("KAFKA_SERVER", server);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(MyConfig.class);
        context.getEnvironment().getPropertySources()
            .addFirst(new MapPropertySource("myProps", props));
        context.refresh();

        ContextUtil.setContext(context);

        new ViewDashboardController();
    }
}

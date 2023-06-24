package br.com.kafkamanager.infrastructure;

import br.com.kafkamanager.infrastructure.preference.UserPreferenceService;
import br.com.kafkamanager.infrastructure.swing.ViewDashboardController;
import br.com.kafkamanager.infrastructure.swing.ViewKafkaConfigController;
import br.com.kafkamanager.infrastructure.swing.util.LookAndFeelUtil;
import br.com.kafkamanager.infrastructure.util.ContextUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MapPropertySource;

public class Main {

    public static String folder = "";

    public static void main(String[] args) {
        receiveArgs(args);

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

    private static void receiveArgs(String[] args) {
        Options options = new Options();
        Option folderToProducers = new Option("folder", "folder", true, "provide producers folder ");
        options.addOption(folderToProducers);

        CommandLineParser parser = new BasicParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            Main.folder = Optional.ofNullable(cmd.getOptionValue(folder)).orElse("models");
        } catch (ParseException e) {
            System.out.println(e.getMessage());

            System.exit(1);
        }
    }
}

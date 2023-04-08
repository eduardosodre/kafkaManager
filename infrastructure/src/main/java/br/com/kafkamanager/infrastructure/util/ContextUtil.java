package br.com.kafkamanager.infrastructure.util;

import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.springframework.context.ApplicationContext;

@UtilityClass
public class ContextUtil {

    @Setter
    private static ApplicationContext context;

    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }
}

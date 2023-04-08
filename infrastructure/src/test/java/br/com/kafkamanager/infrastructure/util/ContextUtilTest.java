package br.com.kafkamanager.infrastructure.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

@ExtendWith(MockitoExtension.class)
class ContextUtilTest {

    @Mock
    private ApplicationContext context;

    @BeforeEach
    public void setUp() {
        ContextUtil.setContext(context);
    }

    @Test
    void shouldGetBean() {
        String expected = "someBeanInstance";
        SomeBean someBean = new SomeBean(expected);
        when(context.getBean(SomeBean.class)).thenReturn(someBean);

        SomeBean result = ContextUtil.getBean(SomeBean.class);

        assertEquals(expected, result.getValue());
    }

    private static class SomeBean {

        private final String value;

        public SomeBean(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}

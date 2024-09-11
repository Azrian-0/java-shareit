package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class ShareItAppTest {

    @Test
    void testMainMethod() throws Exception {
        Method mainMethod = ShareItApp.class.getMethod("main", String[].class);

        Assertions.assertNotNull(mainMethod);

        mainMethod.invoke(null, (Object) new String[] {});
    }
}

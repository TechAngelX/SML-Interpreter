package sml.instruction;

import org.junit.jupiter.api.Test;
import sml.Method;
import sml.Translator;

import java.io.IOException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class TranslatorTest {

    @Test
    public void testReadAndTranslate_validFile() throws IOException {
        Translator translator = new Translator();
        Collection<Method> methods = translator.readAndTranslate("src/test/resources/test1.sml");
        assertNotNull(methods);
        assertFalse(methods.isEmpty());
    }


}

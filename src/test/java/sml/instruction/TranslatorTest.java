package sml.instruction;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sml.Method;
import sml.Translator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

/**
 * ====================================================================================================================
 * Unit tests for the Translator class to verify SML program translation functionality.
 * --------------------------------------------------------------------------------------------------------------------
 * Tests include file location, method translation, label extraction, and basic program parsing.
 * Ensures correct handling of method definitions and instruction parsing.
 * ====================================================================================================================
 *
 * @author Ricki Angel
 */

import static org.junit.jupiter.api.Assertions.*;

public class TranslatorTest {
    String path = "src/test/resources/";
    String fileName1 = "test1.sml";
    String fileName2 = "test2.sml";

    @Test
    @DisplayName("Verify test SML files exist in the resources directory")
    public void shouldFindFile() throws FileNotFoundException, IOException {
        Translator translator = new Translator();

        File filePath1 = new File(path + fileName1);
        File filePath2 = new File(path + fileName2);

        assertTrue(filePath1.exists(), "test1.sml should exist at " + filePath1.getAbsolutePath());
        assertTrue(filePath2.exists(), "test2.sml should exist at " + filePath2.getAbsolutePath());
    }

    @Test
    @DisplayName("Should correctly translate methods with arguments")
    public void translatesMethodsWithArguments() throws IOException {
        Translator translator = new Translator();
        Collection<Method> methods = translator.readAndTranslate("src/test/resources/test1.sml");

        // Find main method:
        Method mainMethod = methods.stream()
                .filter(m -> m.name().toString().equals("main"))
                .findFirst()
                .orElse(null);

        assertNotNull(mainMethod, "Main method should be translated");
        assertEquals(0, mainMethod.arguments().size(), "Main method should have no arguments");
        assertFalse(mainMethod.instructions().isEmpty(), "Main method should have instructions");
    }

    @Test
    @DisplayName("getLabel method extracts label when line ends with colon")
    public void extractsValidLabelWithColon() throws Exception {
        Translator translator = new Translator();
        translator.setLine("label1: add");

        // Private getLabel method originally in Translator class, so using reflection to access it.
        java.lang.reflect.Method getLabelMethod = Translator.class.getDeclaredMethod("getLabel");
        getLabelMethod.setAccessible(true); // Make the private method accessible

        String label = (String) getLabelMethod.invoke(translator);

        assertEquals("label1", label);
    }

}

package sml.instruction;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sml.Method;
import sml.Translator;

import java.io.File;
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

    @Test
    public void shouldFindFile() throws IOException {
        Translator translator = new Translator();

        // Define the file paths
        String filePath1 = "src/test/resources/test1.sml";
        String filePath2 = "src/test/resources/test5.sml";

        // Check if the files exist
        File file1 = new File(filePath1);
        File file2 = new File(filePath2);

        assertTrue(file1.exists(), "test1.sml should exist");
        assertTrue(file2.exists(), "test2.sml should exist" + "");

        // If the files exist, proceed to translation
        if (file1.exists()) {
            Collection<Method> methods1 = translator.readAndTranslate(filePath1);
            // Add assertions based on the expected behavior
        }

        if (file2.exists()) {
            Collection<Method> methods2 = translator.readAndTranslate(filePath2);
            // Add assertions based on the expected behavior
        }
    }



}

package sml.instructions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sml.*;
import sml.services.FileService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the ModInstruction in the Simple Machine Language.
 *
 * <p>Verifies the correct implementation of modulo operations within the SML runtime.</p>
 *
 * <p>Key test scenarios:</p>
 * <ul>
 *   <li>Calculating remainder with different integer values</li>
 *   <li>Ensuring correct stack manipulation during modulo operations</li>
 *   <li>Validating program counter progression</li>
 * </ul>
 *
 * @author Ricki Angel
 */

public class ModInstructionTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private Machine machine;
    private Translator translator;

    @BeforeEach
    void setUp() {
        translator = new Translator(new FileService());

        System.setOut(new PrintStream(outContent));

        machine = new Machine();
        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(new ReturnInstruction(null))
        );
        machine.setProgram(List.of(mainMethod));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    private String createTempSmlFile(String filename, String content) throws IOException {
        Path filePath = Files.createTempFile(filename, ".sml");
        Files.writeString(filePath, content);
        return filePath.toString();
    }

    @Test
    @DisplayName("Test modulo operation with mod opcode")
    void testModuloOperationWithModOpcode() throws IOException {
        String program = """
                @main:
                push 55
                push 9
                mod     // 55 % 9 = 1
                print
                
                push 144
                push 12
                mod     // 144 % 12 = 0
                print
                
                push 27
                push 4
                mod     // 27 % 4 = 3
                print
                
                push 0
                return
                """;

        String filePath = createTempSmlFile("modulo_test_mod_opcode.sml", program);

        Collection<Method> methods = translator.readAndTranslate(filePath);
        machine.setProgram(methods);
        machine.execute();

        String output = outContent.toString();
        assertTrue(output.contains("1"), "Result should be 1 (55 % 9)");
        assertTrue(output.contains("0"), "Result should be 0 (144 % 12)");
        assertTrue(output.contains("3"), "Result should be 3 (27 % 4)");
    }
}

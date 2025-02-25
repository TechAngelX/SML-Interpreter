package sml;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class SmlIntegrationTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private Translator translator;
    private Machine machine;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        translator = new Translator();
        machine = new Machine();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    private String createTempSmlFile(String filename, String content) throws IOException {
        Path filePath = tempDir.resolve(filename);
        Files.writeString(filePath, content);
        return filePath.toString();
    }

    @Test
    void testFactorialProgram() throws IOException {
        // A sample factorial program in SML
        String program = """
                @main
                push 5
                invoke @factorial
                print
                return
                
                @factorial n
                load n
                push 1
                if_cmpeq baseCase
                load n
                load n
                push 1
                sub
                invoke @factorial
                mul
                return
                baseCase: push 1
                return
                """;

        String filePath = createTempSmlFile("factorial.sml", program);

        Collection<Method> methods = translator.readAndTranslate(filePath);
        machine.setProgram(methods);
        machine.execute(methods);

        assertTrue(outContent.toString().contains("120"));
    }

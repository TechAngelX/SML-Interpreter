package sml.instructions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import sml.Translator;
import sml.Machine;
import sml.Method;

import java.io.File;
import java.util.Collection;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.IOException;

public class SmlProgramTranslationAndExecutionTest {
    private Translator translator;
    private Machine machine;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        translator = new Translator();
        machine = new Machine();
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testFullProgramExecution() throws IOException {
        // PWD to verify file path
        System.err.println("Current Working Directory: " + System.getProperty("user.dir"));

        File resourceDir = new File("src/test/resources");
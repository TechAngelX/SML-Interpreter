package sml;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    /**
     * Creates a real life temporary SML file with the given content.
     * <p>
     * This method writes the provided SML program content to a file in a JUnit-managed
     * temporary directory. These files are created in your system's standard temporary
     * directory (typically /tmp on Unix or C:\Users\\username\AppData\Local\Temp on Windows)
     * and are automatically deleted when the test completes.
     * </p>
     *
     * @param filename The name to give the temporary file
     * @param content  The SML program content to write to the file
     * @return The absolute path to the created file
     * @throws IOException If an error occurs while creating or writing to the file
     *
     * @author Ricki Angel
     */
    private String createTempSmlFile(String filename, String content) throws IOException {
        Path filePath = tempDir.resolve(filename);
        Files.writeString(filePath, content);
        return filePath.toString();
    }

    @Test
    @DisplayName("Test arithmetic operations (addition, subtraction, multiplication, division) and correct output")
    void testArithmeticOperations() throws IOException {
        String program = """
            @main:
            push 200
            push 50
            add     // 200 + 50 = 250
            print
            
            push 37
            push 6
            sub     // 37 - 6 = 31
            print
            
            push 6
            push 8
            mul     // 6 * 8 = 48
            print
            
            push 100
            push 4
            div     // 100 / 4 = 25
            print
            
            push 0  // Dummy value for return instruction to pop
            return
            """;

        String filePath = createTempSmlFile("arithmetic.sml", program);

        Collection<Method> methods = translator.readAndTranslate(filePath);
        machine.setProgram(methods);
        machine.execute();

        String output = outContent.toString();
        assertTrue(output.contains("250"), "Output of the addition result should be 250 (200+50)");
        assertTrue(output.contains("31"), "Output of the subtraction result should be 31 (37-6)");
        assertTrue(output.contains("48"), "Output of the multiplication result should be 48 (6*8)");
        assertTrue(output.contains("25"), "Output of the division result should be 25 (100/4)");
    }

    @Test
    @DisplayName("Verifies variable store and load operations with arithmetic computation")
    void testVariableOperations() throws IOException {
        String program = """
        @main:
        push 42
        store testVar
        push 8
        store otherVar
        load testVar
        load otherVar
        add
        print
        push 0  // Dummy push to prevent empty stack on return
        return
        """;

        String filePath = createTempSmlFile("variables.sml", program);

        Collection<Method> methods = translator.readAndTranslate(filePath);
        machine.setProgram(methods);
        machine.execute();

        String output = outContent.toString();
        assertTrue(output.contains("42"));
        assertTrue(output.contains("8"));
        assertTrue(output.contains("50"));
    }

    @Test
    @DisplayName("Test various jump instructions in SML, verifying correct conditional and unconditional jumps")
    void testJumpInstructions() throws IOException {
        String program = """
                @main:
                push 10
                push 20
                if_cmpeq notTakenJump     // 10 != 20, so this jump shoul NOT happen
                push 30
                goto unconditionalJump    // This jump SHOULD taken
                
                notTakenJump: push 999
                print
                
                unconditionalJump: push 40
                push 40
                if_cmpeq equalJump
                push 999
                
                equalJump: push 50
                push 25
                if_cmpgt greaterJump      // 50 > 25, so this jump should be taken
                push 999                  // This should never be executed
                
                greaterJump: push 60
                print                     // Should print 60
                return
                """;

        String filePath = createTempSmlFile("jumps.sml", program);
        Collection<Method> methods = translator.readAndTranslate(filePath);
        machine.setProgram(methods);
        machine.execute();

        // If all jumps work correctly, only 60 should be printed:
        String output = outContent.toString();
        assertFalse(output.contains("999"));
        assertTrue(output.contains("60"));
    }

    @Test
    @DisplayName("Should correctly calculate the 8th Fibonacci number")
    void testFibonacciProgram() throws IOException {
        // A Fibonacci program in SML, similar to the test1.sml:
        String program = """
        @main:
        push 8
        invoke @fib
        print
        push 0      // Push a dummy value for the return instruction
        return
        
        @fib: n
        load n
        push 2
        if_cmpgt recursive  // if n > 2, go to recursive case
        push 1            // base case: fib(1) = fib(2) = 1
        return
        
        recursive: load n
        push 1
        sub
        invoke @fib
        load n
        push 2
        sub
        invoke @fib
        add              // so it's now should be fib(n-1) + fib(n-2)
        return
        """;

        String filePath = createTempSmlFile("fibonacci.sml", program);

        Collection<Method> methods = translator.readAndTranslate(filePath);
        machine.setProgram(methods);
        machine.execute();

        String output = outContent.toString();

        String lastLine = output.lines()
                .filter(line -> line.matches("\\d+"))
                .reduce((first, second) -> second)
                .orElse("");
        assertEquals("21", lastLine, "The 8th Fibonacci number should be 21, but got: " + lastLine);
    }

    @Test
    @DisplayName("Should correctly calculate factorial(5) using recursion")
    void testRecursiveFactorialCalculation() throws IOException {
        String program = """
            @main:
            push 5
            invoke @factorial
            print
            push 0      // Add a dummy value for return to pop
            return
            
            @factorial: n
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
        machine.execute();

        assertTrue(outContent.toString().contains("120"),  "Expected output to contain factorial(5)=120, but didn't find it");
    }


    /**
     * A test to validate nested method call functionality in SML interpreter.
     *
     * @throws IOException
     * @author Ricki Angel
     */
    @Test
    void testNestedMethodCalls() throws IOException {
        String program = """
            @main
            push 5
            push 10
            invoke @inner
            print
            push 0        // Dummy push to prevent empty stack on return
            return
            
            @inner:
            add
            print
            push 0        // Another Dummy push to prevent empty stack on return
            return
            """;

        String filePath = createTempSmlFile("nested.sml", program);

        Collection<Method> methods = translator.readAndTranslate(filePath);

        // TODO ...figure out/design for loop to parse methods...

    }
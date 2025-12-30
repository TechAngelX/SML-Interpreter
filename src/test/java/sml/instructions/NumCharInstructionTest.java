package sml.instructions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sml.Label;
import sml.Machine;
import sml.Method;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite for the NumCharInstruction in the Simple Machine Language.
 *
 * <p>Validates the functionality of converting numbers to characters within the SML runtime.</p>
 *
 * <p>Key test objectives:</p>
 * <ul>
 *   <li>Verifying correct character output for given numbers</li>
 *   <li>Handling edge case scenarios like zero input</li>
 *   <li>Ensuring proper stack manipulation and program flow</li>
 * </ul>
 *
 * @author Ricki Angel 
 */
class NumCharInstructionTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private Machine machine;

    @BeforeEach
    void setUp() {
        machine = new Machine();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        machine = null;
        System.setOut(originalOut);
    }

    /**
     * Verifies that the {@link NumCharInstruction} correctly converts a number 
     * to its corresponding alphabetic character and prints the result.
     *
     * <p>
     * This test pushes the number 3 onto the stack, executes the instruction, 
     * and asserts that the printed output matches the expected character "C".
     * </p>
     * 
     * @author Ricki Angel
     */

    @Test
    @DisplayName("Should print correct letter for given number")
    void testPrintsCorrectLetterForNumber() {
        NumCharInstruction instruction = new NumCharInstruction(null);
        Instruction returnInstruction = new ReturnInstruction(null);

        Method method = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(instruction, returnInstruction)
        );
        machine.setProgram(List.of(method));
        machine.frame().push(3);

        instruction.execute(machine);

        System.out.flush();

        assertEquals("C\n", outContent.toString());
    }

    @Test
    @DisplayName("Should handle zero as input number")
    void testHandlesZeroNumber() {
        Instruction instruction = new NumCharInstruction(new Label("L1"));
        Instruction returnInstruction = new ReturnInstruction(null);

        Method method = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(instruction, returnInstruction)
        );
        machine.setProgram(List.of(method));

        machine.frame().push(0);

        instruction.execute(machine);

        System.out.flush();

        assertEquals("@\n", outContent.toString());
    }
}

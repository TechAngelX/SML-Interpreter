package sml.instructions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sml.*;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the functionality of PushInstruction in the Simple Machine Language.
 * ==========================================================================
 * Verifies pushing values onto the stack and variables method behavior.
 *
 * @author Ricki Angel
 */
public class PushInstructionTest {
    // An optional stream for capturing console output as a byte array, useful for test verification.
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private Machine machine;

    @BeforeEach
    void setUp() {
        machine = new Machine();
    }

    @AfterEach
    void tearDown() {
        machine = null;
        System.setOut(System.out);
    }

    @Test
    @DisplayName("Should push a value onto the stack")
    void testPushInstructionShouldPushValueOntoStack() {
        Instruction pushInstruction = new PushInstruction(null, 42);
        Instruction returnInstruction = new ReturnInstruction(null);

        Method mainMethod = new Method(new Method.Identifier("@main"),
                List.of(), List.of(pushInstruction, returnInstruction));
        machine.setProgram(List.of(mainMethod));

        pushInstruction.execute(machine);
        int poppedValue = machine.frame().pop();
        assertEquals(42, poppedValue);
    }

    @Test
    @DisplayName("Should return an empty stream for variables() method")
    void testPushInstructionVariablesShouldReturnEmptyStream() {
        Instruction pushInstruction = new PushInstruction(null, 42);
        List<Variable.Identifier> variables = pushInstruction.variables().collect(Collectors.toList());
        assertEquals(0, variables.size());
    }
}

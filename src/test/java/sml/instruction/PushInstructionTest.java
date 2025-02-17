package sml.instruction;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sml.*;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

//   Tests the main functionality PushInstruction.
// - Push a specified value (e.g., 42) onto the machine's operand stack.
// - Correctly add the value to the stack, which is then confirmed by retrieving (popping) it.


public class PushInstructionTest {
    // An optional stream for capturing console output as a byte array, useful for test verification.
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private Machine machine;

    @BeforeEach
    void setUp() {
        // Initialize a new Machine instance before each test.
        machine = new Machine();
    }

    @AfterEach
    void tearDown() {
        // Clean up the Machine instance and reset System.out to its default.
        machine = null;
        System.setOut(System.out);
    }

    @Test
    void pushInstructionShouldPushValueOntoStack() {
        Instruction pushInstruction = new PushInstruction(null, 42);
        Instruction returnInstruction = new ReturnInstruction(null);

        Method mainMethod = new Method(new Method.Identifier("@main"),
                List.of(), List.of(pushInstruction, returnInstruction));
        machine.setProgram(List.of(mainMethod));

        pushInstruction.execute(machine);
        int poppedValue = machine.frame().pop();
        assertEquals(42, poppedValue);
    }

    /**
     * Verifies that the variables() method returns an empty stream for PushInstruction.
     * Since PushInstruction does not reference any variables, the stream should be empty.
     */
    @Test
    void pushInstructionVariablesShouldReturnEmptyStream() {
        Instruction pushInstruction = new PushInstruction(null, 42);
        List<Variable.Identifier> variables = pushInstruction.variables().collect(Collectors.toList());
        assertEquals(0, variables.size());
    }
}

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
 * Test suite for validating the PushInstruction's behavior in the Simple Machine Language.
 *
 * <p>Comprehensive testing of constant value loading operations:</p>
 * <ul>
 *   <li>Correctly pushing literal values onto the operand stack</li>
 *   <li>Handling various integer constant inputs</li>
 *   <li>Verifying stack manipulation accuracy</li>
 * </ul>
 *
 * <p>Validates the push instruction's critical role in initializing
 * computational values and preparing stack-based operations.</p>
 *
 * @author Ricki Angel
 */
public class PushInstructionTest {
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

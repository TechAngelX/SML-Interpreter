package java.sml.instruction;

import org.junit.jupiter.api.BeforeEach;
import sml.*;
import sml.instruction.AddInstruction;
import sml.instruction.ReturnInstruction;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for AddInstruction to see if:
 * - Two numbers can be add correctly
 * - If it can Handle stack operations properly
 * - It if moves to next instruction after adding
 */
class AddInstructionTest {
    private Machine machine;

    @BeforeEach
    void setUp() {
        machine = new Machine();

        // No tearDown needed - all objects will be garbage collected and no system resources were used.    @Test

        // Create a simple method that just returns.
        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(new ReturnInstruction(null))
        );
        machine.setProgram(List.of(mainMethod));
    }

    void testExecuteAddInstruction() {
        // Push two numbers onto stack (in the CORRECT order)
        machine.frame().push(5);  // First number
        machine.frame().push(3);  // Second number

        Instruction instruction = new AddInstruction(null);
        instruction.execute(machine);

        // Pop and check result
        int result = machine.frame().pop();
        assertEquals(8, result, "5 + 3 should equal 8");
    }
}
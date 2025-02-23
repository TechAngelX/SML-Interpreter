package sml.instructions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sml.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the functionality of AddInstruction in the Simple Machine Language.
 * ========================================================================
 * Verifies correct addition of numbers from the stack and instruction progression.
 *
 * @author Ricki Angel
 */
class AddInstructionTest {
    private Machine machine;

    @BeforeEach
    void setUp() {
        machine = new Machine();
    }

    // No tearDown needed - all objects will be garbage collected and no system resources were used.

    @Test
    @DisplayName("Should correctly add two numbers from the stack")
    void testExecuteAddInstruction() {
        Instruction addInstruction = new AddInstruction(null);
        Instruction returnInstruction = new ReturnInstruction(null);

        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(addInstruction, returnInstruction)
        );
        machine.setProgram(List.of(mainMethod));

        machine.frame().push(64);
        machine.frame().push(36);

        Optional<Frame> nextFrame = addInstruction.execute(machine);

        int result = machine.frame().pop();
        assertEquals(100, result, "64 + 36 should equal 100");

        assertTrue(nextFrame.isPresent(), "Next frame should exist");
        assertEquals(1, nextFrame.get().programCounter(), "Program counter should advance to next instruction");
    }
}
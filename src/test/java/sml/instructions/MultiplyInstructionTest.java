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
 * Tests the functionality of MultiplyInstruction in the Simple Machine Language.
 * =============================================================================
 * Verifies correct multiplication of numbers from the stack, and instruction progression.
 *
 * @author Ricki Angel
 */
class MultiplyInstructionTest {
    private Machine machine;

    @BeforeEach
    void setUp() {
        machine = new Machine();
        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(new ReturnInstruction(null))
        );
        machine.setProgram(List.of(mainMethod));
    }

    @Test
    @DisplayName("Should correctly multiply two numbers from the stack")
    void testMultiplyTwoNumbers() {
        // Create instructions for multiplication and program termination:
        Instruction multiplyInstruction = new MultiplyInstruction(null);
        Instruction returnInstruction = new ReturnInstruction(null);

        // Set up the program with both instructions:
        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(multiplyInstruction, returnInstruction)
        );
        // Setup and push operands onto stack in LIFO order:
        machine.setProgram(List.of(mainMethod));
        machine.frame().push(8);  // First operand.
        machine.frame().push(7);  // Second operand (will be popped first).

        // Execute multiplication and get next frame:
        Optional<Frame> nextFrame = multiplyInstruction.execute(machine);

        // Verify correct multiplication result:
        int result = machine.frame().pop();
        assertEquals(56, result, "8 * 7 should equal 56");

        // Verify program advances correctly:
        assertTrue(nextFrame.isPresent(), "Next frame should exist");
        assertEquals(1, nextFrame.get().programCounter(), "Program counter should advance to next instruction");
    }
}
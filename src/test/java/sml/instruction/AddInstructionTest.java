package sml.instruction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sml.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for AddInstruction to see if:
 * - Two numbers can be added correctly
 * - If it can Handle stack operations properly
 * - It if moves to next instruction after adding
 */
class AddInstructionTest {
    private Machine machine;

    @BeforeEach
    void setUp() {
        machine = new Machine();

        // No tearDown needed - all objects will be garbage collected and no system resources were used.

    }
    @Test
    void testExecuteAddInstruction() {
        // Create instructions for addition and program termination
        Instruction addInstruction = new AddInstruction(null);
        Instruction returnInstruction = new ReturnInstruction(null);

        // Set up the program with both instructions
        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(addInstruction, returnInstruction)
        );
        machine.setProgram(List.of(mainMethod));

        // Push operands onto stack in LIFO order
        machine.frame().push(64);  // First operand
        machine.frame().push(36);  // Second operand (will be popped first)

        // Execute addition and get next frame
        Optional<Frame> nextFrame = addInstruction.execute(machine);

        // Verify correct addition result
        int result = machine.frame().pop();
        assertEquals(100, result, "64 + 36 should equal 100");

        // Verify program advances correctly
        assertTrue(nextFrame.isPresent(), "Next frame should exist");
        assertEquals(1, nextFrame.get().programCounter(), "Program counter should advance to next instruction");
    }
}
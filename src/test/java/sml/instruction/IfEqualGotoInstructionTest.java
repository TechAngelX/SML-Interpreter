package sml.instruction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sml.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for IfEqualGotoInstruction to verify:
 * - Correctly compares two values for equality
 * - Jumps to label when values are equal
 * - Continues to next instruction when values are not equal
 * - Handles stack operations properly
 */
class IfEqualGotoInstructionTest {
    private Machine machine;

    @BeforeEach
    void setUp() {
        machine = new Machine();
    }
    /**
     * Verifies {@link IfEqualGotoInstruction} continues to the next instruction
     * when stack values are not equal. Pushes unequal values, executes, and confirms
     * the program counter does not jump to next label/instructionsjhit.
            */
    @Test
    void testShouldJumpToTargetIfValuesAreEqual() {
        // Create labels to repreesnt locations in the programme.
        Label jumpLabel = new Label("jump");
        Label returnLabel = new Label("return");

        // Create instructions
        Instruction ifEqualGotoInstruction = new IfEqualGotoInstruction(null, jumpLabel);
        Instruction jumpTargetInstruction = new ReturnInstruction(jumpLabel);
        Instruction nextInstruction = new ReturnInstruction(returnLabel);

        // Set up program with instructions. Indexing is based on the order put in the List.of() method.
        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(ifEqualGotoInstruction, nextInstruction, jumpTargetInstruction)
        );
        machine.setProgram(List.of(mainMethod));

        // Push equal values onto stack
        machine.frame().push(42);  // First operand.
        machine.frame().push(42);  // Second operand.

        // Execute instruction.
        Optional<Frame> nextFrame = ifEqualGotoInstruction.execute(machine);

        // Verify jump occurred - Should jump to instruction with jumpLabel (jumpTargetInstruction).
        assertTrue(nextFrame.isPresent(), "Next frame should exist");
        int programCounter = nextFrame.get().programCounter();
        assertEquals(2, programCounter, "Should jump to instruction at index 2");
    }

    /**
     * Verifies that the IfEqualGotoInstruction behaves correctly when the two values
     * popped from the stack are not equal. If so the instruction should NOT jump to the
     * specified label but proceed to the next instruction.
     */
    @Test
    void testContinueWhenValuesNotEqual() {
        Label jumpLabel = new Label("jump");
        Label returnLabel = new Label("return");

        Instruction ifEqualGotoInstruction = new IfEqualGotoInstruction(null, jumpLabel);
        Instruction jumpTargetInstruction = new ReturnInstruction(jumpLabel);
        Instruction nextInstruction = new ReturnInstruction(returnLabel);

        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(ifEqualGotoInstruction, jumpTargetInstruction, nextInstruction)
        );
        machine.setProgram(List.of(mainMethod));

        // Push different values onto stack.
        machine.frame().push(99);  // First operand.
        machine.frame().push(2);  // Second operand.

        // Execute.
        Optional<Frame> nextFrame = ifEqualGotoInstruction.execute(machine);

        // Verify did not jump
        assertTrue(nextFrame.isPresent(), "Next frame should exist");
        int programCounter = nextFrame.get().programCounter();
        assertEquals(1, programCounter, "Should continue to next instruction at index 1");    }
}
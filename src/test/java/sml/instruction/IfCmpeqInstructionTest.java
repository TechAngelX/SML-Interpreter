package sml.instruction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sml.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the functionality of IfCmpeqInstruction in the Simple Machine Language.
 * ================================================================================
 * Verifies correct comparison and conditional jumping based on stack value equality.
 *
 * @author Ricki Angel
 */
class IfCmpeqInstructionTest {
    private Machine machine;

    @BeforeEach
    void setUp() {
        machine = new Machine();
    }
    /**
     * Note: verifies {@link IfCmpeqInstruction} continues to the next instruction
     * when stack values are not equal. Pushes unequal values, executes, and confirms
     * the program counter does not jump to next label/instruction.
     */

    @Test
    @DisplayName("Should jump to target label when stack values are equal")
    void testShouldJumpToTargetIfValuesAreEqual() {
        // Create labels to repreesnt locations in the programme:
        Label jumpLabel = new Label("jump");
        Label returnLabel = new Label("return");

        // Create instructions:
        Instruction ifEqualGotoInstruction = new IfCmpeqInstruction(null, jumpLabel);
        Instruction jumpTargetInstruction = new ReturnInstruction(jumpLabel);
        Instruction nextInstruction = new ReturnInstruction(returnLabel);

        // Set up program with instructions. Indexing is based on the order put in the List.of() method.
        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(ifEqualGotoInstruction, nextInstruction, jumpTargetInstruction)
        );
        // Setup program and push equal values onto stack:
        machine.setProgram(List.of(mainMethod));
        machine.frame().push(42);  // First operand.
        machine.frame().push(42);  // Second operand.

        // Execute instruction:
        Optional<Frame> nextFrame = ifEqualGotoInstruction.execute(machine);

        // Verify jump occurred - Should jump to instruction with jumpLabel (jumpTargetInstruction):
        assertTrue(nextFrame.isPresent(), "Next frame should exist");
        int programCounter = nextFrame.get().programCounter();
        assertEquals(2, programCounter, "Should jump to instruction at index 2");
    }

    @Test
    @DisplayName("Should NOT jump when stack values are unequal")
    void testShouldNotJumpToTargetIfValuesAreUnequal() {
        Label jumpLabel = new Label("jump");
        Label returnLabel = new Label("return");

        Instruction ifEqualGotoInstruction = new IfCmpeqInstruction(null, jumpLabel);
        Instruction jumpTargetInstruction = new ReturnInstruction(jumpLabel);
        Instruction nextInstruction = new ReturnInstruction(returnLabel);

        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(ifEqualGotoInstruction, nextInstruction, jumpTargetInstruction)
        );
        machine.setProgram(List.of(mainMethod));
        machine.frame().push(99);  // First operand.
        machine.frame().push(2);  // Second operand.

        Optional<Frame> nextFrame = ifEqualGotoInstruction.execute(machine);

        // Verify did not jump:
        assertTrue(nextFrame.isPresent(), "Next frame should exist");
        int programCounter = nextFrame.get().programCounter();
        assertEquals(1, programCounter, "Should not jump to target, but continue to next instruction at index 1");    }
}
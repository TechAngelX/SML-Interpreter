package sml.instructions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sml.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Test class for the IfCmpgtInstruction in the Simple Machine Language.
 *
 * <p>Validates the functionality of conditional greater-than comparison instructions.</p>
 *
 * <p>Test scenarios cover various comparison conditions:</p>
 * <ul>
 *   <li>Jumping when first value is greater than the second</li>
 *   <li>Continuing sequentially when first value is not greater</li>
 *   <li>Handling equal value comparisons</li>
 * </ul>
 *
 * @author Ricki Angel
 */

public class IfCmpeqInstructionTest {
    private Machine machine;

    @BeforeEach
    void setUp() {
        machine = new Machine();
    }

    /**
     * Verifies {@link IfCmpeqInstruction} continues to the next instruction
     * when stack values are not equal. Pushes unequal values, executes, and confirms
     * the program counter does not jump to next label/instruction.
     */

    @Test
    @DisplayName("Should jump to target label when stack values are equal")
    void testShouldJumpToTargetIfValuesAreEqual() {
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
        machine.frame().push(42);
        machine.frame().push(42);

        Optional<Frame> nextFrame = ifEqualGotoInstruction.execute(machine);

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
        machine.frame().push(99);
        machine.frame().push(2);

        Optional<Frame> nextFrame = ifEqualGotoInstruction.execute(machine);

        assertTrue(nextFrame.isPresent(), "Next frame should exist");
        int programCounter = nextFrame.get().programCounter();
        assertEquals(1, programCounter, "Should not jump to target, but continue to next instruction at index 1");
    }
}

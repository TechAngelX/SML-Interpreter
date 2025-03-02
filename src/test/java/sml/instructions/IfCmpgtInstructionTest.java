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

public class IfCmpgtInstructionTest {
    private Machine machine;

    @BeforeEach
    void setUp() {
        machine = new Machine();
    }

    /**
     * Verifies that the {@link IfCmpgtInstruction} correctly advances to the next instruction
     * (at index 1) when the first value popped from the stack is larger than the second.
     * <p>
     * This test confirms that the instruction proceeds sequentially when the comparison condition
     * (first value greater than second) is met. Specifically, it validates the non-jump behavior
     * by ensuring that the program counter advances to the instruction immediately following
     * the comparison instruction, which is located at index 1 in the instruction list.
     * </p>
     */
    @Test
    @DisplayName("Should jump when first value is greater than second")
    void testShouldJumpWhenFirstValueGreater() {
        Label jumpLabel = new Label("jump");
        Label returnLabel = new Label("return");

        Instruction ifGreaterGotoInstruction = new IfCmpgtInstruction(null, jumpLabel);
        Instruction nextInstruction = new ReturnInstruction(returnLabel);
        Instruction jumpTargetInstruction = new ReturnInstruction(jumpLabel);

        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(ifGreaterGotoInstruction, nextInstruction, jumpTargetInstruction)
        );
        machine.setProgram(List.of(mainMethod));

        machine.frame().push(10);
        machine.frame().push(5);

        Optional<Frame> nextFrame = ifGreaterGotoInstruction.execute(machine);

        assertTrue(nextFrame.isPresent(), "Next frame should exist");
        int programCounter = nextFrame.get().programCounter();
        assertEquals(2, programCounter, "Should jump to the jumptarget instruction at index 2");
    }

    @Test
    @DisplayName("Should continue to next instruction when first value is not greater")
    void testContinueToNextInstructionWhenFirstValueNotGreater() {
        Label jumpLabel = new Label("jump");
        Label returnLabel = new Label("return");

        Instruction ifGreaterGotoInstruction = new IfCmpgtInstruction(null, jumpLabel);
        Instruction nextInstruction = new ReturnInstruction(returnLabel);
        Instruction jumpTargetInstruction = new ReturnInstruction(jumpLabel);

        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(ifGreaterGotoInstruction, nextInstruction, jumpTargetInstruction)
        );
        machine.setProgram(List.of(mainMethod));
        machine.frame().push(36);
        machine.frame().push(101);

        Optional<Frame> nextFrame = ifGreaterGotoInstruction.execute(machine);

        assertTrue(nextFrame.isPresent(), "Next frame should exist");
        int programCounter = nextFrame.get().programCounter();
        assertEquals(1, programCounter, "if first value smaller than second, should not jump, but continue to next instruction");
    }

    @Test
    @DisplayName("Should continue to next instruction when values are equal")
    void testContinueToNextInstructionIfFirstAndSecondValueAreEqual() {
        Label jumpLabel = new Label("jump");
        Label returnLabel = new Label("return");

        Instruction ifGreaterGotoInstruction = new IfCmpgtInstruction(null, jumpLabel);
        Instruction nextInstruction = new ReturnInstruction(returnLabel);
        Instruction jumpTargetInstruction = new ReturnInstruction(jumpLabel);

        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(ifGreaterGotoInstruction, nextInstruction, jumpTargetInstruction)
        );
        machine.setProgram(List.of(mainMethod));
        machine.frame().push(4);
        machine.frame().push(4);

        Optional<Frame> nextFrame = ifGreaterGotoInstruction.execute(machine);

        assertTrue(nextFrame.isPresent(), "Next frame should exist");
        int programCounter = nextFrame.get().programCounter();
        assertEquals(1, programCounter, "If equal, should not jump, but continue to the next instruction at index 1");
    }
}

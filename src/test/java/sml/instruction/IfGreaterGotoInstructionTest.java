package sml.instruction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sml.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the functionality of IfGreaterGotoInstruction in the Simple Machine Language.
 * ===================================================================================
 * Verifies correct comparison and conditional jumping based on stack value comparison.
 */
class IfGreaterGotoInstructionTest {
    private Machine machine;

    @BeforeEach
    void setUp() {
        machine = new Machine();
    }
    /**
     * NoteL verifies that the {@link IfGreaterGotoInstruction} correctly advances to the next instruction
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

        // Create instruction objects. IfGreaterGotoInstruction will jump to jumpLabel.
        // if the first value popped from the stack is greater than the second value popped.
        Instruction ifGreaterGotoInstruction = new IfGreaterGotoInstruction(null, jumpLabel);
        Instruction nextInstruction = new ReturnInstruction(returnLabel); // Next instruction after comparison
        Instruction jumpTargetInstruction = new ReturnInstruction(jumpLabel); // Instruction to jump to

        // Set up the program with instructions:
        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(ifGreaterGotoInstruction, nextInstruction, jumpTargetInstruction)
        );
        machine.setProgram(List.of(mainMethod));

        // Basically, when first number popped from the stack is largar than second number, the test checks
        // that the program jumps to the jumptarget instruction - the third instruction, which is at index 2.

        machine.frame().push(10);  // First operand.
        machine.frame().push(5);   // Second operand.

        // Execute instruction
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

        Instruction ifGreaterGotoInstruction = new IfGreaterGotoInstruction(null, jumpLabel);
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

        // Execute:
        Optional<Frame> nextFrame = ifGreaterGotoInstruction.execute(machine);

        // Verify did not jump:
        assertTrue(nextFrame.isPresent(), "Next frame should exist");
        int programCounter = nextFrame.get().programCounter();
        assertEquals(1, programCounter, "if first value smaller than second, should not jump, but continue to next instruction");
    }

    @Test
    @DisplayName("Should continue to next instruction when values are equal")
    void testContinueToNextInstructionIfFirstAndSecondValueAreEqual() {
        Label jumpLabel = new Label("jump");
        Label returnLabel = new Label("return");

        Instruction ifGreaterGotoInstruction = new IfGreaterGotoInstruction(null, jumpLabel);
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
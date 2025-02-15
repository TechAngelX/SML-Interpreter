package sml.instruction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sml.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for IfGreaterGotoInstruction to verify:
 * - Correctly compares two values
 * - Jumps to label when first value is greater
 * - Continues to next instruction when first value is not greater
 * - Handles stack operations properly
 */
class IfGreaterGotoInstructionTest {
    private Machine machine;

    @BeforeEach
    void setUp() {
        machine = new Machine();
    }

    @Test
    void testJumpWhenFirstValueGreater() {
        // Create labels.
        Label jumpLabel = new Label("jump");
        Label returnLabel = new Label("return");

        // Create instruction objects. IfGreaterGotoInstruction will jump to jumpLabel
        // if the first value popped from the stack is greater than the second value popped.
        Instruction ifGreaterGotoInstruction = new IfGreaterGotoInstruction(null, jumpLabel);
        Instruction jumpTargetInstruction = new ReturnInstruction(jumpLabel);
        Instruction nextInstruction = new ReturnInstruction(returnLabel);


        // Set up the program with instructions
        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(ifGreaterGotoInstruction, jumpTargetInstruction, nextInstruction)
        );
        machine.setProgram(List.of(mainMethod));


        //Basically, when first number popped from the stack is largar than second number, the test
        // checks that the program moves to the next instruction, which is at index 1.
        // This should verifies that the jump instruction works correctly by advancing to the instruction
        // immediately following the comparison when comparison condition is met.

        machine.frame().push(10);  // First operand
        machine.frame().push(5);   // Second operand

        // Execute instruction
        Optional<Frame> nextFrame = ifGreaterGotoInstruction.execute(machine);

        // Verify jump occurred
        assertTrue(nextFrame.isPresent(), "Next frame should exist");

        // Check that the current instruction is the jump target instruction
        Instruction currentInstruction = mainMethod.instructions().get(nextFrame.get().programCounter());
        assertEquals(jumpTargetInstruction, currentInstruction, "Should jump to the correct instruction");
    }
    @Test
    void testContinueWhenFirstValueNotGreater() {
        // Create labels to repreesent locations in the programme.
        Label jumpLabel = new Label("jump");
        Label returnLabel = new Label("return");

        // Create instructions
        Instruction ifGreaterGotoInstruction = new IfGreaterGotoInstruction(null, jumpLabel);
        Instruction jumpTargetInstruction = new ReturnInstruction(jumpLabel);
        Instruction nextInstruction = new ReturnInstruction(returnLabel);

        // Set up the program with instructions
        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(ifGreaterGotoInstruction, jumpTargetInstruction, nextInstruction)
        );
        machine.setProgram(List.of(mainMethod));

        // Push values onto stack (first value not greater)
        machine.frame().push(5);   // First operand
        machine.frame().push(10);  // Second operand

        // Execute instruction
        Optional<Frame> nextFrame = ifGreaterGotoInstruction.execute(machine);

        // Verify did not jump
        assertTrue(nextFrame.isPresent(), "Next frame should exist");

        // Check that the current instruction is the next instruction
        Instruction currentInstruction = mainMethod.instructions().get(nextFrame.get().programCounter());
        assertEquals(nextInstruction, currentInstruction, "Should continue to next instruction");
    }

    @Test
    void testWenEqualValues() {
        Label jumpLabel = new Label("jump");
        Label returnLabel = new Label("return");

        Instruction ifGreaterGotoInstruction = new IfGreaterGotoInstruction(null, jumpLabel);
        Instruction jumpTargetInstruction = new ReturnInstruction(jumpLabel);
        Instruction nextInstruction = new ReturnInstruction(returnLabel);

        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(ifGreaterGotoInstruction, jumpTargetInstruction, nextInstruction)
        );
        machine.setProgram(List.of(mainMethod));

        // Push equal values onto stack
        machine.frame().push(7);   // First operand
        machine.frame().push(7);   // Second operand

        // Execute instruction
        Optional<Frame> nextFrame = ifGreaterGotoInstruction.execute(machine);

        // Verify did not jump
        assertTrue(nextFrame.isPresent(), "Next frame should exist");

        // Check that the current instruction is the next instruction
        Instruction currentInstruction = mainMethod.instructions().get(nextFrame.get().programCounter());
        assertEquals(nextInstruction, currentInstruction, "Should continue to next instruction");
    }
}
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
        // Create labels
        Label jumpLabel = new Label("jump");
        Label returnLabel = new Label("return");

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

        //Basically, when first number on the stack is larrger than second number, the test
        // checks that the programme moves to the next instruction, which is at index 1.
        // This should verifies that the jump instruction works correctly by advancing to the instruction
        // immediately following the comparison when comparison condition is met.

        machine.frame().push(10);  // First operand
        machine.frame().push(5);   // Second operand

        // Execute instruction
        Optional<Frame> nextFrame = ifGreaterGotoInstruction.execute(machine);

        // Verify jumped to correct instruction
        assertTrue(nextFrame.isPresent(), "Next frame should exist");
        assertEquals(1, nextFrame.get().programCounter(), "Should jump to jump target instruction");
    }

    @Test
    void testContinueWhenFirstValueNotGreater() {
        // Create labels
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

        // Verify continued to next instruction
        assertTrue(nextFrame.isPresent(), "Next frame should exist");
        assertEquals(1, nextFrame.get().programCounter(), "Should continue to next instruction");
    }

    @Test
    void testEqualValues() {
        // Create labels
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

        // Push equal values onto stack
        machine.frame().push(7);   // First operand
        machine.frame().push(7);   // Second operand

        // Execute instruction
        Optional<Frame> nextFrame = ifGreaterGotoInstruction.execute(machine);

        // Verify continued to next instruction
        assertTrue(nextFrame.isPresent(), "Next frame should exist");
        assertEquals(1, nextFrame.get().programCounter(), "Should continue to next instruction");
    }
}
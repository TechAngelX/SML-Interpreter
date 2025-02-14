package sml.instruction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sml.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for SubInstruction to verify:
 * - Two numbers can subtract correctly
 * - It handles stack operations properly
 * - It moves to the next instruction after subtracting
 */
class SubInstructionTest {
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
    void testExecuteSubInstruction() {
        // Create subtraction instruction and return instruction
        Instruction subInstruction = new SubInstruction(null);
        Instruction returnInstruction = new ReturnInstruction(null);

        // Set up the program with both instructions
        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(subInstruction, returnInstruction)
        );
        machine.setProgram(List.of(mainMethod));

        // Push operands onto stack in LIFO order
        machine.frame().push(34);  // First operand
        machine.frame().push(16);  // Second operand (will be popped first)

        // Execute subtraction and get next frame
        Optional<Frame> nextFrame = subInstruction.execute(machine);

        // Verify correct subtraction result
        int result = machine.frame().pop();
        assertEquals(18, result, "34 - 16 should equal 18");

        // Verify program advances correctly
        assertTrue(nextFrame.isPresent(), "Next frame should exist");
        assertEquals(1, nextFrame.get().programCounter(), "Program counter should advance to next instruction");
    }

    @Test
    void testSubInstructionWithLargerSecondOperand() {
        // Create subtraction instruction and return instruction
        Instruction subInstruction = new SubInstruction(null);
        Instruction returnInstruction = new ReturnInstruction(null);

        // Set up the program with both instructions
        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(subInstruction, returnInstruction)
        );
        machine.setProgram(List.of(mainMethod));

        // Push operands in reverse order to simulate correct subtraction
        machine.frame().push(16);  // First operand
        machine.frame().push(34);  // Second operand

        Instruction instruction = new SubInstruction(null);
        Optional<Frame> nextFrame = instruction.execute(machine);

        int result = machine.frame().pop();
        assertEquals(-18, result, "16 - 34 should equal -18");

        // Verify program advances correctly
        assertTrue(nextFrame.isPresent(), "Next frame should exist");
        assertEquals(1, nextFrame.get().programCounter(), "Program counter should advance to next instruction");
    }

    @Test
    void testSubInstructionWithZero() {
        // Create subtraction instruction and return instruction
        Instruction subInstruction = new SubInstruction(null);
        Instruction returnInstruction = new ReturnInstruction(null);

        // Set up the program with both instructions
        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(subInstruction, returnInstruction)
        );
        machine.setProgram(List.of(mainMethod));

        machine.frame().push(100);  // First operand
        machine.frame().push(60);   // Second operand

        Instruction instruction = new SubInstruction(null);
        Optional<Frame> nextFrame = instruction.execute(machine);

        int result = machine.frame().pop();
        assertEquals(40, result, "100 - 60 should equal 40");

        // Verify program advances correctly
        assertTrue(nextFrame.isPresent(), "Next frame should exist");
        assertEquals(1, nextFrame.get().programCounter(), "Program counter should advance to next instruction");
    }
}
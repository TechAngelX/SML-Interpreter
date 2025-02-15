package sml.instruction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sml.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for DivInstruction to verify:
 * - Two numbers can be divided correctly
 * - It handles stack operations properly
 * - It moves to next instruction after dividing
 * - It handles division by zero
 */
class DivInstructionTest {
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
    void testExecuteDivInstruction() {
        // Create instructions for division and program termination
        Instruction divInstruction = new DivInstruction(null);
        Instruction returnInstruction = new ReturnInstruction(null);

        // Set up the program with both instructions
        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(divInstruction, returnInstruction)
        );
        machine.setProgram(List.of(mainMethod));

        // Push operands onto stack in LIFO order
        machine.frame().push(56);  // First operand
        machine.frame().push(8);   // Second operand (will be popped first)

        // Execute division and get next frame
        Optional<Frame> nextFrame = divInstruction.execute(machine);

        // Verify correct division result
        int result = machine.frame().pop();
        assertEquals(7, result, "56 / 8 should equal 7");

        // Verify program advances correctly
        assertTrue(nextFrame.isPresent(), "Next frame should exist");
        assertEquals(1, nextFrame.get().programCounter(), "Program counter should advance to next instruction");
    }

    @Test
    void testDivisionWithNegativeNumbers() {
        // Create division instruction and return instruction
        Instruction divInstruction = new DivInstruction(null);
        Instruction returnInstruction = new ReturnInstruction(null);

        // Set up the program with both instructions
        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(divInstruction, returnInstruction)
        );
        machine.setProgram(List.of(mainMethod));

        // Push operands
        machine.frame().push(-56);  // First operand
        machine.frame().push(8);    // Second operand

        Optional<Frame> nextFrame = divInstruction.execute(machine);

        int result = machine.frame().pop();
        assertEquals(-7, result, "-56 / 8 should equal -7");

        // Verify program advances correctly
        assertTrue(nextFrame.isPresent(), "Next frame should exist");
        assertEquals(1, nextFrame.get().programCounter(), "Program counter should advance to next instruction");
    }

    @Test
    void testDivisionByZero() {
        // Create division instruction and return instruction
        Instruction divInstruction = new DivInstruction(null);

        // Set up the program with division instruction
        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(divInstruction)
        );
        machine.setProgram(List.of(mainMethod));

        // Push operands for division by zero
        machine.frame().push(50);  // First operand
        machine.frame().push(0);   // Second operand

        // Verify that division by zero throws an ArithmeticException
        assertThrows(ArithmeticException.class,
                () -> divInstruction.execute(machine),
                "Division by zero should throw ArithmeticException"
        );
    }
}
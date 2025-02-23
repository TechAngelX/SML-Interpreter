package sml.instructions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sml.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the functionality of DivInstruction in the Simple Machine Language.
 * ========================================================================
 * Verifies correct division of numbers from the stack, including edge cases
 * like negative numbers and division by zero.
 *
 * @author Ricki Angel
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
    @DisplayName("Should correctly divide two numbers from the stack")
    void testExecuteDivInstruction() {
        Instruction divInstruction = new DivInstruction(null);
        Instruction returnInstruction = new ReturnInstruction(null);

        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(divInstruction, returnInstruction)
        );
        machine.setProgram(List.of(mainMethod));

        machine.frame().push(56);
        machine.frame().push(8);

        Optional<Frame> nextFrame = divInstruction.execute(machine);

        int result = machine.frame().pop();
        assertEquals(7, result, "56 / 8 should equal 7");

        assertTrue(nextFrame.isPresent(), "Next frame should exist");
        assertEquals(1, nextFrame.get().programCounter(), "Program counter should advance to next instruction");
    }

    @Test
    @DisplayName("Should handle division with negative numbers")
    void testDivisionWithNegativeNumbers() {
        Instruction divInstruction = new DivInstruction(null);
        Instruction returnInstruction = new ReturnInstruction(null);

        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(divInstruction, returnInstruction)
        );
        machine.setProgram(List.of(mainMethod));

        machine.frame().push(-56);
        machine.frame().push(8);

        Optional<Frame> nextFrame = divInstruction.execute(machine);

        int result = machine.frame().pop();
        assertEquals(-7, result, "-56 / 8 should equal -7");

        assertTrue(nextFrame.isPresent(), "Next frame should exist");
        assertEquals(1, nextFrame.get().programCounter(), "Program counter should advance to next instruction");
    }

    @Test
    @DisplayName("Should throw ArithmeticException when dividing by zero")
    void testDivisionByZero() {
        Instruction divInstruction = new DivInstruction(null);

        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(divInstruction)
        );
        machine.setProgram(List.of(mainMethod));

        machine.frame().push(50);
        machine.frame().push(0);

        assertThrows(ArithmeticException.class,
                () -> divInstruction.execute(machine),
                "Division by zero should throw ArithmeticException"
        );
    }
}
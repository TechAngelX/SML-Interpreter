package sml.instructions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sml.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for verifying the SubInstruction's behavior in the Simple Machine Language.
 *
 * <p>Comprehensive testing of subtraction instruction functionality:</p>
 * <ul>
 *   <li>Accurate subtraction of stack-based operands</li>
 *   <li>Handling of positive and negative integer subtraction</li>
 *   <li>Correct stack manipulation during subtraction</li>
 *   <li>Ensuring proper program counter progression</li>
 * </ul>
 *
 * <p>Validates the subtraction instruction's correctness across various
 * computational scenarios and edge cases.</p>
 *
 * @author Ricki Angel
 */

public class SubInstructionTest {
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
    @DisplayName("Should correctly subtract two numbers from the stack")
    void testExecuteSubInstruction() {
        Instruction subInstruction = new SubInstruction(null);
        Instruction returnInstruction = new ReturnInstruction(null);

        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(subInstruction, returnInstruction)
        );
        machine.setProgram(List.of(mainMethod));

        machine.frame().push(34);
        machine.frame().push(16);

        Optional<Frame> nextFrame = subInstruction.execute(machine);

        int result = machine.frame().pop();
        assertEquals(18, result, "34 - 16 should equal 18");

        assertTrue(nextFrame.isPresent(), "Next frame should exist");
        assertEquals(1, nextFrame.get().programCounter(), "Program counter should advance to next instruction");
    }

    @Test
    @DisplayName("Should handle subtraction with larger second operand")
    void testSubInstructionWithLargerSecondOperand() {
        Instruction subInstruction = new SubInstruction(null);
        Instruction returnInstruction = new ReturnInstruction(null);

        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(subInstruction, returnInstruction)
        );
        machine.setProgram(List.of(mainMethod));

        machine.frame().push(16);
        machine.frame().push(34);

        Instruction instruction = new SubInstruction(null);
        Optional<Frame> nextFrame = instruction.execute(machine);

        int result = machine.frame().pop();
        assertEquals(-18, result, "16 - 34 should equal -18");

        assertTrue(nextFrame.isPresent(), "Next frame should exist");
        assertEquals(1, nextFrame.get().programCounter(), "Program counter should advance to next instruction");
    }

    @Test
    @DisplayName("Should handle subtraction with zero")
    void testSubInstructionWithZero() {
        Instruction subInstruction = new SubInstruction(null);
        Instruction returnInstruction = new ReturnInstruction(null);

        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(subInstruction, returnInstruction)
        );
        machine.setProgram(List.of(mainMethod));

        machine.frame().push(100);
        machine.frame().push(60);

        Instruction instruction = new SubInstruction(null);
        Optional<Frame> nextFrame = instruction.execute(machine);

        int result = machine.frame().pop();
        assertEquals(40, result, "100 - 60 should equal 40");

        assertTrue(nextFrame.isPresent(), "Next frame should exist");
        assertEquals(1, nextFrame.get().programCounter(), "Program counter should advance to next instruction");
    }
}

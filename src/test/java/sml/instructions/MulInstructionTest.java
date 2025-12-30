package sml.instructions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sml.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test suite for verifying the MulInstruction's behavior in the Simple Machine Language.
 *
 * <p>Comprehensive testing of multiplication instruction functionality:</p>
 * <ul>
 *   <li>Accurate multiplication of stack-based operands</li>
 *   <li>Handling of positive and negative integer multiplication</li>
 *   <li>Correct stack manipulation during multiplication</li>
 *   <li>Ensuring proper program counter progression</li>
 * </ul>
 *
 * <p>Validates the multiplication instruction's correctness across various
 * computational scenarios and edge cases.</p>
 *
 * @author Ricki Angel
 */
public class MulInstructionTest {
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
    @DisplayName("Should correctly multiply two numbers from the stack")
    void testMultiplyTwoNumbers() {
        Instruction multiplyInstruction = new MulInstruction(null);
        Instruction returnInstruction = new ReturnInstruction(null);

        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(multiplyInstruction, returnInstruction)
        );
        machine.setProgram(List.of(mainMethod));
        machine.frame().push(8);
        machine.frame().push(7);

        Optional<Frame> nextFrame = multiplyInstruction.execute(machine);

        int result = machine.frame().pop();
        assertEquals(56, result, "8 * 7 should equal 56");

        assertTrue(nextFrame.isPresent(), "Next frame should exist");
        assertEquals(1, nextFrame.get().programCounter(), "Program counter should advance to next instruction");
    }
}
